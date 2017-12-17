package net.rokyinfo.ops.monitorman.entity;

import lombok.Data;
import net.rokyinfo.ops.monitorman.service.AlarmService;

import java.util.List;

/**
 * @author yuanzhijian
 */

@Data
public class HandlerEntity {

    private Long id;

    private List<AlarmService> alarmServiceList;


}
