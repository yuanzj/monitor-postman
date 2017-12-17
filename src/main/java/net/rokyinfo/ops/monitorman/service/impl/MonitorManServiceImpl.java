package net.rokyinfo.ops.monitorman.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import net.rokyinfo.ops.monitorman.entity.IntervalTypeEnum;
import net.rokyinfo.ops.monitorman.entity.MonitorManEntity;
import net.rokyinfo.ops.monitorman.entity.TestsuiteListEntity;
import net.rokyinfo.ops.monitorman.exception.CheckException;
import net.rokyinfo.ops.monitorman.model.MonitorManForm;
import net.rokyinfo.ops.monitorman.service.HandlerService;
import net.rokyinfo.ops.monitorman.service.MonitorManService;
import net.rokyinfo.ops.monitorman.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yuanzhijian
 */
@Service
public class MonitorManServiceImpl implements MonitorManService {

    private static final Logger logger = LoggerFactory.getLogger(MonitorManServiceImpl.class);

    private static ConcurrentMap<Long, ScheduledFuture<?>> scheduledFutureCache = new ConcurrentHashMap<>();

    private static ConcurrentMap<Long, MonitorManEntity> monitorManEntityCache = new ConcurrentHashMap<>();

    @Autowired
    private Sequence monitorIdSequence;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Value("${monitor.storage.directory}")
    private String storageDirectory;

    @Value("${monitor.datasource.file}")
    private String jsonFileName;

    @Value("${monitor.check-result-files-max-reserve-count}")
    private int maxReserveCount;


    @Override
    @Async
    public void initMonitorListFromFileData() {

        File file = new File(storageDirectory, jsonFileName + ".json");
        if (file.exists() && file.isFile()) {
            List<MonitorManEntity> monitorManEntityList = JacksonUtil.readValue(file, new TypeReference<List<MonitorManEntity>>() {
            });
            if (monitorManEntityList != null) {
                monitorManEntityList.forEach(monitorManEntity -> {
                    monitorManEntityCache.put(monitorManEntity.getId(), monitorManEntity);
                    if (monitorManEntity.isRunning()) {
                        startMonitor(monitorManEntity.getId());
                    } else {
                        stopMonitor(monitorManEntity.getId());
                    }
                });
            }
        }

    }

    @Override
    public Long createMonitorMan(MonitorManForm monitorManForm) {

        // 检查文件
        if (monitorManForm.getCollectionFile() == null) {
            throw new CheckException("postman collection JSON file can not be null");
        } else if (monitorManForm.getEnvironmentFile() == null) {
            throw new CheckException("postman export environment file can not be null");
        }

        // 检查文件后缀
        String suffix = "json";
        if (!monitorManForm.getCollectionFile().getOriginalFilename().endsWith(suffix)) {
            throw new CheckException("postman collection JSON file format error");
        } else if (!monitorManForm.getEnvironmentFile().getOriginalFilename().endsWith(suffix)) {
            throw new CheckException("postman export environment file format error");
        }

        // 监控项创建
        MonitorManEntity monitorManEntity = new MonitorManEntity();
        monitorManEntity.setId(monitorIdSequence.nextId());
        monitorManEntity.setMonitorName(monitorManForm.getMonitorName());
        monitorManEntity.setIntervalType(IntervalTypeEnum.getByValue(monitorManForm.getIntervalType()));
        monitorManEntity.setInterval(monitorManForm.getInterval());

        // 初始化目录
        String storageParentPath = storageDirectory + "/" + monitorManEntity.getId() + "/";
        File storageDirectoryFile = new File(storageParentPath);
        storageDirectoryFile.mkdirs();

        // 运行脚本、环境变量文件存储
        File collectionFile = new File(storageParentPath, monitorManForm.getCollectionFile().getOriginalFilename());
        File environmentFile = new File(storageParentPath, monitorManForm.getEnvironmentFile().getOriginalFilename());
        try {
            monitorManForm.getCollectionFile().transferTo(collectionFile);
            monitorManEntity.setCollectionFilePath(collectionFile.getAbsolutePath());

            monitorManForm.getEnvironmentFile().transferTo(environmentFile);
            monitorManEntity.setEnvironmentFilePath(environmentFile.getAbsolutePath());
        } catch (IllegalStateException | IOException e) {
            logger.error("err:" + e);
        }

        //创建时间
        monitorManEntity.setCreateTime(new Date());
        monitorManEntity.setMaxReserveCount(maxReserveCount);
        monitorManEntity.setStorageParentPath(storageParentPath);

        monitorManEntityCache.put(monitorManEntity.getId(), monitorManEntity);

        return monitorManEntity.getId();
    }

    @Override
    public Boolean updateMonitorMan(MonitorManForm monitorManForm) {

        if (!monitorManEntityCache.containsKey(monitorManForm.getId())) {
            throw new CheckException("monitor is not exist");
        }
        MonitorManEntity monitorManEntity = monitorManEntityCache.get(monitorManForm.getId());

        // 监控名称更新
        if (!StringUtils.isEmpty(monitorManForm.getMonitorName())) {
            monitorManEntity.setMonitorName(monitorManForm.getMonitorName());
        }

        // 定时器更新
        if (monitorManForm.getIntervalType() != null && monitorManForm.getInterval() != null) {
            monitorManEntity.setIntervalType(IntervalTypeEnum.getByValue(monitorManForm.getIntervalType()));
            monitorManEntity.setInterval(monitorManForm.getInterval());
        }

        // 初始化目录
        String suffix = "json";
        File storageDirectoryFile = new File(monitorManEntity.getStorageParentPath());
        storageDirectoryFile.mkdirs();

        // 运行脚本更新
        if (monitorManForm.getCollectionFile() != null) {

            if (!monitorManForm.getCollectionFile().getOriginalFilename().endsWith(suffix)) {
                throw new CheckException("file format error");
            }

            new File(monitorManEntity.getCollectionFilePath()).delete();

            File collectionFile = new File(monitorManEntity.getStorageParentPath(), monitorManForm.getCollectionFile().getOriginalFilename());
            try {
                monitorManForm.getCollectionFile().transferTo(collectionFile);
                monitorManEntity.setCollectionFilePath(collectionFile.getAbsolutePath());
            } catch (IllegalStateException | IOException e) {
                logger.error("err:" + e);

            }

        }
        // 环境变量更新
        if (monitorManForm.getEnvironmentFile() != null) {

            if (!monitorManForm.getEnvironmentFile().getOriginalFilename().endsWith(suffix)) {
                throw new CheckException("file format error");
            }

            new File(monitorManEntity.getEnvironmentFilePath()).delete();

            File environmentFile = new File(monitorManEntity.getStorageParentPath(), monitorManForm.getEnvironmentFile().getOriginalFilename());
            try {
                monitorManForm.getEnvironmentFile().transferTo(environmentFile);
                monitorManEntity.setEnvironmentFilePath(environmentFile.getAbsolutePath());
            } catch (IllegalStateException | IOException e) {
                logger.error("err:" + e);

            }

        }

        // 更新时间
        monitorManEntity.setUpdateTime(new Date());

        monitorManEntityCache.put(monitorManEntity.getId(), monitorManEntity);

        if (monitorManEntity.isRunning()) {
            startMonitor(monitorManForm.getId());
        }
        return true;
    }

    @Override
    public Boolean deleteMonitorMan(Long monitorId) {
        if (!monitorManEntityCache.containsKey(monitorId)) {
            throw new CheckException("monitor is not exist");
        }
        //停止任务
        stopMonitor(monitorId);
        //删除文件夹
        String storageParentPath = storageDirectory + "/" + monitorId + "/";
        FileUtil.deleteDir(new File(storageParentPath));
        //删除缓存
        monitorManEntityCache.remove(monitorId);
        scheduledFutureCache.remove(monitorId);
        return true;
    }

    @Override
    public Collection<MonitorManEntity> getMonitorManList() {
        return monitorManEntityCache.values();
    }

    @Override
    public MonitorManEntity getMonitorDetail(Long id) {
        return monitorManEntityCache.get(id);
    }

    @Override
    public Boolean startMonitor(Long monitorId) {
        // 先停止，再开启.
        stopMonitor(monitorId);
        MonitorManEntity monitorManEntity = monitorManEntityCache.get(monitorId);
        if (monitorManEntity != null) {
            ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new MonitorManTask(monitorManEntity), new CronTrigger(MonitorUtil.getCron(monitorManEntity)));
            scheduledFutureCache.put(monitorId, future);
            monitorManEntity.setRunning(true);
            monitorManEntity.setRunTime(new Date());
        }

        return true;
    }

    @Override
    public Boolean stopMonitor(Long monitorId) {
        ScheduledFuture<?> future = scheduledFutureCache.get(monitorId);
        if (future != null) {
            future.cancel(true);
            monitorManEntityCache.get(monitorId).setRunning(false);
        }

        return true;
    }

    static class MonitorManTask implements Runnable {

        private MonitorManEntity monitorManEntity;

        MonitorManTask(MonitorManEntity monitorManEntity) {
            this.monitorManEntity = monitorManEntity;
        }

        @Override
        public void run() {

            logger.info("execute monitor task :" + monitorManEntity.getMonitorName());
            //启动另外一个线程执行任务确保定时器能够按照设置的时间进行运行
            ThreadPoolExecutor newmanExecThreadPoolExecutor = SpringContextUtil.getBean("newmanExecThreadPoolExecutor", ThreadPoolExecutor.class);
            newmanExecThreadPoolExecutor.execute(() -> {

                monitorManEntity.setLastExecuteTime(new Date());
                long reportId = System.currentTimeMillis();

                // 存储路径初始化
                StringBuilder checkOutDirectory = new StringBuilder(monitorManEntity.getStorageParentPath());
                String checkOutParentPath = checkOutDirectory.append("checkOut/").toString();
                String checkOutFilePath = checkOutDirectory.append(reportId).append("/").toString();
                String checkOutJunitXmlOutFilePath = checkOutDirectory.append("xmlOut.xml").toString();
                String htmlReport = "/" + monitorManEntity.getId() + "/checkOut/" + reportId + "/htmlOut.html";
                
                // 删除历史文件，确保文件不把磁盘撑爆
                MonitorUtil.delOldMonitorCheckResult(checkOutParentPath, monitorManEntity.getMaxReserveCount());

                // 执行newman 命令
                MonitorUtil.execNewMan(monitorManEntity.getCollectionFilePath(), monitorManEntity.getEnvironmentFilePath(), checkOutFilePath);
                try {
                    //解析newman 输出的junit测试报告
                    TestsuiteListEntity testsuiteListEntity = (TestsuiteListEntity) XmlUtil.xmlToBean(checkOutJunitXmlOutFilePath, TestsuiteListEntity.class);
                    if (!MonitorUtil.junitResultPass(testsuiteListEntity)) {

                        //进行后续处理
                        logger.info("find failure testcase , send  email.");
                        HandlerService handlerService = SpringContextUtil.getBean("handlerServiceImpl", HandlerService.class);
                        handlerService.execute(monitorManEntity, testsuiteListEntity, htmlReport);

                    }

                } catch (JAXBException | IOException e) {
                    logger.error(e.getMessage());
                }

            });
        }
    }

}
