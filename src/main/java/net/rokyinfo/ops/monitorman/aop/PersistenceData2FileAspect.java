package net.rokyinfo.ops.monitorman.aop;

import net.rokyinfo.ops.monitorman.entity.MonitorManEntity;
import net.rokyinfo.ops.monitorman.service.MonitorManService;
import net.rokyinfo.ops.monitorman.util.FileUtil;
import net.rokyinfo.ops.monitorman.util.JacksonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;


/**
 * @author yuanzhijian
 */
@Aspect
@Component
public class PersistenceData2FileAspect {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceData2FileAspect.class);

    @Autowired
    private MonitorManService monitorManService;

    @Value("${monitor.storage.directory}")
    private String storageDirectory;

    @Value("${monitor.datasource.file}")
    private String jsonFileName;


    @Pointcut("@annotation(net.rokyinfo.ops.monitorman.annotation.PersistenceData2File)")
    public void saveDataPointCut() {

    }

    @After("saveDataPointCut()")
    @Async
    public void persistenceData2File(JoinPoint joinPoint) {

        Collection<MonitorManEntity> monitorManEntities = monitorManService.getMonitorManList();
        String json = JacksonUtil.toJSon(monitorManEntities);
        if (json != null) {
            logger.info("createJsonFile");
            FileUtil.createJsonFile(json, storageDirectory, jsonFileName);
        }
    }

}
