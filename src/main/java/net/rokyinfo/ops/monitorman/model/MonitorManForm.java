package net.rokyinfo.ops.monitorman.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author yuanzhijian
 */
@Data
public class MonitorManForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 监控名称
     */
    @NotNull(message = "monitor name can not be null")
    private String monitorName;

    /**
     * Postman Collection JSON file
     */
    private MultipartFile collectionFile;

    /**
     * 脚本执行间隔类型(0 ：秒 1：分钟 2：小时)
     */
    @NotNull(message = "interval type can not be null")
    private Integer intervalType;

    /**
     * 脚本执行间隔时间
     */
    @NotNull(message = "interval time can not be null")
    private Integer interval;

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
    private MultipartFile iterationDataFile;

    /**
     * Postman export environment file
     */
    private MultipartFile environmentFile;

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

}
