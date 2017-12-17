package net.rokyinfo.ops.monitorman.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author yuanzhijian
 */
@XmlRootElement(name = "testsuites")
public class TestsuiteListEntity {

    private List<TestsuiteEntity> testsuiteEntityList;

    @XmlElement(name = "testsuite")
    public List<TestsuiteEntity> getTestsuiteEntityList() {
        return testsuiteEntityList;
    }

    public void setTestsuiteEntityList(List<TestsuiteEntity> testsuiteEntityList) {
        this.testsuiteEntityList = testsuiteEntityList;
    }
}
