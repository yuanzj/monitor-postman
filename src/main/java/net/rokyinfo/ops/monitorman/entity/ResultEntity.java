package net.rokyinfo.ops.monitorman.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @param <T>
 * @author yuanzhijian
 */
@Data
public class ResultEntity<T> implements Serializable {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final int NO_PERMISSION = 2;
    private static final long serialVersionUID = 1L;
    private String msg = "success";

    private int code = SUCCESS;

    private T data;

    public ResultEntity() {
        super();
    }

    public ResultEntity(T data) {
        super();
        this.data = data;
    }

    public ResultEntity(Throwable e) {
        super();
        this.msg = e.toString();
        this.code = FAIL;
    }
}