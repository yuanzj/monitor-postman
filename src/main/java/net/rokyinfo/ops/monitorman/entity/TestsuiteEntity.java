package net.rokyinfo.ops.monitorman.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author yuanzhijian
 */
public class TestsuiteEntity {

    private String name;
    private String id;
    private String tests;
    private String time;

    private List<TestcaseEntity> testcaseEntityList;

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute(name = "tests")
    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    @XmlAttribute(name = "time")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @XmlElement(name = "testcase")
    public List<TestcaseEntity> getTestcaseEntityList() {
        return testcaseEntityList;
    }

    public void setTestcaseEntityList(List<TestcaseEntity> testcaseEntityList) {
        this.testcaseEntityList = testcaseEntityList;
    }
}
