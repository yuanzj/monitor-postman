package net.rokyinfo.ops.monitorman.entity;

import lombok.Data;

import java.util.List;

/**
 * @author yuanzhijian
 */
@Data
public class MessageEntity {

    /**
     * 收件人
     */
    private String[] recipients;

    /**
     * 主题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;


}
