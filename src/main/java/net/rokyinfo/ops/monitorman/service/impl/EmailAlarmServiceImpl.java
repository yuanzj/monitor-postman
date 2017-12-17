package net.rokyinfo.ops.monitorman.service.impl;

import net.rokyinfo.ops.monitorman.entity.MessageEntity;
import net.rokyinfo.ops.monitorman.extend.AlarmMailSender;
import net.rokyinfo.ops.monitorman.service.AlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuanzhijian
 */
@Service("emailAlarmServiceImpl")
public class EmailAlarmServiceImpl implements AlarmService {

    private static final Logger logger = LoggerFactory.getLogger(EmailAlarmServiceImpl.class);

    @Autowired
    private AlarmMailSender alarmMailSender;

    @Override
    public void alarm(MessageEntity messageEntity) {

        logger.info("send email");

        String[] recipients = messageEntity.getRecipients();
        String subject = messageEntity.getSubject();
        String content = messageEntity.getContent();

        Map<String, Object> data = new HashMap<>();
        data.put("alarmContent", content);

        try {
            alarmMailSender.sendMail(recipients, subject, "MailTemplate", data, null);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
