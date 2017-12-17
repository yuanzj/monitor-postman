package net.rokyinfo.ops.monitorman.entity;


import lombok.Data;

import java.util.Date;

/**
 * @author yuanzhijian
 */
@Data
public class MonitorManEntity {


    /**
     * 是否正在运行
     */
    private boolean isRunning;

    /**
     * ID
     */
    private Long id;

    /**
     * 监控名称
     */
    private String monitorName;

    /**
     * Postman Collection JSON file
     */
    private String collectionFilePath;

    /**
     * 脚本执行间隔类型(0 ：秒 1：分钟 2：小时)
     */
    private IntervalTypeEnum intervalType;

    /**
     * 脚本执行间隔时间
     */
    private int interval;

    /**
     * 请求超时时间（MS）
     * 0 indicate infinity
     */
    private int timeoutRequest;

    /**
     * 重复次数
     */
    private int iterationCount;

    /**
     * 执行参数
     * JSON or CSV file to be used as data source when running multiple iterations on a collection
     */
    private String iterationDataFilePath;

    /**
     * Postman export environment file
     */
    private String environmentFilePath;

    /**
     * 存储路径
     */
    private String storageParentPath;

    /**
     * 后续处理流程ID
     */
    private Long handlerId;

    /**
     * This specifies whether newman would automatically follow 3xx responses from servers.
     */
    private boolean ignoreRedirects;

    /**
     * Disables SSL verification checks and allows self-signed SSL certificates.
     */
    private boolean insecure;

    /**
     * Switch to specify whether or not to gracefully stop a collection run on encountering the first error.
     */
    private boolean bail;

    /**
     * create time
     */
    private Date createTime;

    /**
     * run time
     */
    private Date runTime;

    /**
     * update time
     */
    private Date updateTime;

    /**
     * last execute count
     */
    private Date lastExecuteTime;

    /**
     * max reserve count
     */
    private int maxReserveCount;

}
