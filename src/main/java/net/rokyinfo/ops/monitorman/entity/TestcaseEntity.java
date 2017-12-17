package net.rokyinfo.ops.monitorman.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author yuanzhijian
 */
public class TestcaseEntity {
    private String name;
    private String time;
    private FailureEntity failureEntity;

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "time")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @XmlElement(name = "failure")
    public FailureEntity getFailureEntity() {
        return failureEntity;
    }

    public void setFailureEntity(FailureEntity failureEntity) {
        this.failureEntity = failureEntity;
    }
}
