package net.rokyinfo.ops.monitorman.entity;

import net.rokyinfo.ops.monitorman.exception.CheckException;

/**
 * @author yuanzhijian
 */
public enum IntervalTypeEnum {

    /**
     * 秒
     */
    SECOND(0),

    /**
     * 分
     */
    MINUTE(1),

    /**
     * 时
     */
    HOUR(2);

    int value;

    IntervalTypeEnum(int value) {
        this.value = value;
    }

    public static IntervalTypeEnum getByValue(int value) {
        for (IntervalTypeEnum typeEnum : IntervalTypeEnum.values()) {
            if (typeEnum.value == value) {
                return typeEnum;
            }
        }
        throw new CheckException("No element matches " + value);
    }

    public int getValue() {
        return value;
    }

}
