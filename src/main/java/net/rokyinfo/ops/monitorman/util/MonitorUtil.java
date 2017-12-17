package net.rokyinfo.ops.monitorman.util;

import net.rokyinfo.ops.monitorman.entity.*;
import net.rokyinfo.ops.monitorman.exception.CheckException;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author yuanzhijian
 */
public class MonitorUtil {

    /**
     * 获取cron 表达式
     *
     * @param monitorManEntity
     * @return
     */
    public static String getCron(MonitorManEntity monitorManEntity) {

        if (monitorManEntity.getInterval() > 0) {
            if (monitorManEntity.getIntervalType() == IntervalTypeEnum.SECOND) {
                return "0/" + monitorManEntity.getInterval() + " * * * * *";
            } else if (monitorManEntity.getIntervalType() == IntervalTypeEnum.MINUTE) {
                return "0 0/" + monitorManEntity.getInterval() + " * * * *";
            } else if (monitorManEntity.getIntervalType() == IntervalTypeEnum.HOUR) {
                return "0 0 0/" + monitorManEntity.getInterval() + " * * *";
            }
        }

        throw new CheckException("can not gen cron");
    }

    /**
     * 删除历史监控结果
     *
     * @param path            文件所在路径
     * @param reserveMaxCount 保留文件数量
     */
    public static void delOldMonitorCheckResult(String path, int reserveMaxCount) {
        File[] files = new File(path).listFiles(File::isDirectory);
        if (files != null) {
            List<File> list = Arrays.asList(files);

            if (list.size() > 0) {

                list.sort((file, newFile) -> {
                    if (file.lastModified() < newFile.lastModified()) {
                        return 1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return -1;
                    }

                });

            }

            for (int i = 0; i < list.size(); i++) {
                if (i >= reserveMaxCount) {
                    FileUtil.deleteDir(list.get(i));
                }
            }
        }
    }

    /**
     * 检查监控结果
     *
     * @param testsuiteListEntity
     * @return true 正常 false 异常
     */
    public static boolean junitResultPass(TestsuiteListEntity testsuiteListEntity) {
        if (testsuiteListEntity != null && testsuiteListEntity.getTestsuiteEntityList() != null) {

            boolean pass = true;
            for (TestsuiteEntity testsuiteEntity : testsuiteListEntity.getTestsuiteEntityList()) {

                if (testsuiteEntity.getTestcaseEntityList() != null) {
                    for (TestcaseEntity testcaseEntity : testsuiteEntity.getTestcaseEntityList()) {

                        if (testcaseEntity.getFailureEntity() != null) {
                            pass = false;
                            break;
                        }
                    }
                }

            }
            return pass;
        } else {
            return false;
        }
    }

    /**
     * 构建告警内容
     *
     * @param testsuiteListEntity
     * @return
     */
    public static String buildAlarmContent(TestsuiteListEntity testsuiteListEntity) {

        StringBuilder stringBuilder = new StringBuilder();
        if (testsuiteListEntity != null && testsuiteListEntity.getTestsuiteEntityList() != null) {

            for (TestsuiteEntity testsuiteEntity : testsuiteListEntity.getTestsuiteEntityList()) {

                if (testsuiteEntity.getTestcaseEntityList() != null) {
                    for (TestcaseEntity testcaseEntity : testsuiteEntity.getTestcaseEntityList()) {

                        if (testcaseEntity.getFailureEntity() != null) {
                            stringBuilder.append(testsuiteEntity.getName());
                            stringBuilder.append("[");
                            stringBuilder.append(testcaseEntity.getName());
                            stringBuilder.append(" Failure！]");
                        }
                    }
                }

            }

        }

        return stringBuilder.toString();
    }


    /**
     * 执行newman 命令
     *
     * @param postmanCollection
     * @param postmanEnvironment
     * @param parentPath
     * @return
     */
    public static String execNewMan(String postmanCollection, String postmanEnvironment, String parentPath) {

//        newman run /opt/data/jenkins_home/workspace/测试环境-管理接口自动化测试/骑多多-后台管理接口.postman_collection.json
// --environment /opt/data/jenkins_home/workspace/测试环境-管理接口自动化测试/测试环境.postman_environment.json
// --reporters cli,html,json,junit
// --reporter-json-export /opt/data/jenkins_home/workspace/测试环境-管理接口自动化测试/jsonOut.json
// --reporter-junit-export /opt/data/jenkins_home/workspace/测试环境-管理接口自动化测试/xmlOut.xml
// --reporter-html-export /opt/data/jenkins_home/workspace/测试环境-管理接口自动化测试/htmlOut.html
        String jsonOut = parentPath + "jsonOut.json";
        String xmlOut = parentPath + "xmlOut.xml";
        String htmlOut = parentPath + "htmlOut.html";

        return ExecCmdUtil.execLinuxCmd("newman run " + postmanCollection
                + " --environment " + postmanEnvironment
                + " --reporters cli,html,json,junit "
                + " --reporter-json-export " + jsonOut
                + " --reporter-junit-export " + xmlOut
                + " --reporter-html-export " + htmlOut);
    }

}
