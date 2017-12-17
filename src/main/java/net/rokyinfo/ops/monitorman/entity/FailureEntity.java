package net.rokyinfo.ops.monitorman.entity;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author yuanzhijian
 */
public class FailureEntity {

    private String type;

    @XmlAttribute(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
