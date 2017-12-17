package net.rokyinfo.ops.monitorman.service;

import net.rokyinfo.ops.monitorman.entity.MessageEntity;

/**
 * @author yuanzhijian
 */
public interface AlarmService {

    /**
     * 告警
     *
     * @param messageEntity
     */
    void alarm(MessageEntity messageEntity);

}
