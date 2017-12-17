package net.rokyinfo.ops.monitorman.service;

import net.rokyinfo.ops.monitorman.entity.HandlerEntity;
import net.rokyinfo.ops.monitorman.entity.MonitorManEntity;
import net.rokyinfo.ops.monitorman.entity.TestsuiteListEntity;

/**
 * @author yuanzhijian
 */
public interface HandlerService {

    /**
     * 根据ID 获取出错后的告警处理
     *
     * @param id
     * @return
     */
    HandlerEntity getHandlerById(Long id);


    /**
     * 根据handlerID 查找后续处理流程并进行相关处理
     *
     * @param monitorManEntity
     * @param testsuiteListEntity
     * @param htmlReport
     *
     */
    void execute(MonitorManEntity monitorManEntity, TestsuiteListEntity testsuiteListEntity,String htmlReport);

}
