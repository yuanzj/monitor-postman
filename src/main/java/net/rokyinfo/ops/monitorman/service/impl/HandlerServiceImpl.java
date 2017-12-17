package net.rokyinfo.ops.monitorman.service.impl;

import net.rokyinfo.ops.monitorman.entity.HandlerEntity;
import net.rokyinfo.ops.monitorman.entity.MessageEntity;
import net.rokyinfo.ops.monitorman.entity.MonitorManEntity;
import net.rokyinfo.ops.monitorman.entity.TestsuiteListEntity;
import net.rokyinfo.ops.monitorman.service.AlarmService;
import net.rokyinfo.ops.monitorman.service.HandlerService;
import net.rokyinfo.ops.monitorman.util.MonitorUtil;
import net.rokyinfo.ops.monitorman.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author yuanzhijian
 */
@Service("handlerServiceImpl")
public class HandlerServiceImpl implements HandlerService {

    private static final Logger logger = LoggerFactory.getLogger(HandlerServiceImpl.class);

    private static ConcurrentMap<Long, HandlerEntity> handlerEntityConcurrentMap = new ConcurrentHashMap<>();

    @Value("${monitor.recipients.email}")
    private String emailRecipients;

    @Value("${monitor.recipients.sms}")
    private String smsRecipients;

    @Value("${monitor.alarm.subject}")
    private String alarmSubject;

    private String host;

    @Value("${server.port}")
    private String port;

    public HandlerServiceImpl() {
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("get server host Exception e:", e);
        }
    }

    @Override
    public synchronized HandlerEntity getHandlerById(Long id) {

        if (handlerEntityConcurrentMap.isEmpty()) {
            HandlerEntity handlerEntity = new HandlerEntity();
            handlerEntity.setId(0L);
            ArrayList<AlarmService> alarmServiceArrayList = new ArrayList<>();
            alarmServiceArrayList.add(SpringContextUtil.getBean("emailAlarmServiceImpl", EmailAlarmServiceImpl.class));
            handlerEntity.setAlarmServiceList(alarmServiceArrayList);

            handlerEntityConcurrentMap.put(id, handlerEntity);
        }
        return handlerEntityConcurrentMap.get(id);
    }

    @Override
    public void execute(MonitorManEntity monitorManEntity, TestsuiteListEntity testsuiteListEntity, String htmlReport) {
        // 暂时写死0L支持邮件和短信告警，后续从monitorManEntity取得ID支持配置
        HandlerEntity handlerEntity = getHandlerById(0L);

        if (handlerEntity != null) {
            String[] emailRecipientsArray = this.emailRecipients.split(",");
            String[] smsRecipientsArray = this.smsRecipients.split(",");

            if (handlerEntity.getAlarmServiceList() != null) {
                handlerEntity.getAlarmServiceList().forEach(alarmService -> {
                    MessageEntity messageEntity = new MessageEntity();

                    if (alarmService instanceof EmailAlarmServiceImpl) {
                        messageEntity.setRecipients(emailRecipientsArray);
                        messageEntity.setSubject(alarmSubject);
                        String content = monitorManEntity.getMonitorName() +
                                MonitorUtil.buildAlarmContent(testsuiteListEntity) +
                                "详细测试报告: http://" + host + ":" + port + "/res" + htmlReport;
                        messageEntity.setContent(content);
                    }

                    alarmService.alarm(messageEntity);
                });
            }
        }
    }

}
