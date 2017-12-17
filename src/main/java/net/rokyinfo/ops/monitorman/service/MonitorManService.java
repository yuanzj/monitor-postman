package net.rokyinfo.ops.monitorman.service;

import net.rokyinfo.ops.monitorman.entity.MonitorManEntity;
import net.rokyinfo.ops.monitorman.model.MonitorManForm;

import java.util.Collection;

/**
 * @author yuanzhijian
 */
public interface MonitorManService {

    /**
     * init monitor list from JSON file
     */
    void initMonitorListFromFileData();


    /**
     * 创建监控
     *
     * @param monitorManForm
     * @return
     */
    Long createMonitorMan(MonitorManForm monitorManForm);

    /**
     * 创建监控
     *
     * @param monitorManForm
     * @return
     */
    Boolean updateMonitorMan(MonitorManForm monitorManForm);

    /**
     * 删除监控
     *
     * @param monitorId
     * @return
     */
    Boolean deleteMonitorMan(Long monitorId);

    /**
     * 获取监控列表
     *
     * @return
     */
    Collection<MonitorManEntity> getMonitorManList();

    /**
     * 监控详情
     *
     * @param id
     * @return
     */
    MonitorManEntity getMonitorDetail(Long id);

    /**
     * 开始监控
     *
     * @param monitorId
     * @return
     */
    Boolean startMonitor(Long monitorId);

    /**
     * 停止监控
     *
     * @param monitorId
     * @return
     */
    Boolean stopMonitor(Long monitorId);


}
