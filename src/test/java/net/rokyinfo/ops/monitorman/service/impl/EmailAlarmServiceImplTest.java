package net.rokyinfo.ops.monitorman.service.impl;

import net.rokyinfo.ops.monitorman.entity.MessageEntity;
import net.rokyinfo.ops.monitorman.service.AlarmService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class EmailAlarmServiceImplTest {

    @Autowired
    private AlarmService alarmService;

    @Test
    public void alarm() throws Exception {
//        MessageEntity messageEntity = new MessageEntity();
//        List<String> recipients = new ArrayList<>();
//        recipients.add("zhijian.yuan@rokyinfo.com");
//        messageEntity.setRecipients(recipients);
//        messageEntity.setSubject("监控报警");
//        messageEntity.setContent("test");
//
//
//        alarmService.alarm(messageEntity);
    }

}