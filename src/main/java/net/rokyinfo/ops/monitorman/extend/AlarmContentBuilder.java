package net.rokyinfo.ops.monitorman.extend;

import java.util.Map;

/**
 * 邮寄内容生成器
 *
 * @author yuanzhijian
 */
public interface AlarmContentBuilder {

    /**
     * 根据模板生成邮件
     *
     * @param templateName
     * @param datas
     * @return
     */
    String buildMessage(String templateName, Map<String, Object> datas);

}
