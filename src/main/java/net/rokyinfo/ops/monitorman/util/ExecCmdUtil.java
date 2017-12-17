package net.rokyinfo.ops.monitorman.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * 执行命令
 *
 * @author yuanzhijian
 */
public class ExecCmdUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExecCmdUtil.class);


    /**
     * 执行linux 命令
     *
     * @param cmd
     * @return
     */
    public static String execLinuxCmd(String cmd) {
        try {
            String[] cmdA = {"/bin/sh", "-c", cmd};
            Process process = Runtime.getRuntime().exec(cmdA);
            LineNumberReader br = new LineNumberReader(new InputStreamReader(
                    process.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
//                logger.info(line);
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
