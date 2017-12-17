package net.rokyinfo.ops.monitorman.config;

import net.rokyinfo.ops.monitorman.entity.ResultEntity;
import net.rokyinfo.ops.monitorman.exception.CheckException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuanzhijian
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultEntity errorHandler(HttpServletRequest req, Exception e) {
        ResultEntity<?> result = new ResultEntity();
        // 已知异常
        if (e instanceof CheckException) {
            result.setMsg(e.getLocalizedMessage());
            result.setCode(ResultEntity.FAIL);
        } else if (e instanceof BindException) {
            BindException exception = (BindException) e;
            exception.getFieldErrors();
            if (exception.getFieldErrors().size() > 0) {
                result.setMsg(exception.getFieldErrors().get(0).getDefaultMessage());
            } else {
                result.setMsg(e.getLocalizedMessage());
            }
            result.setCode(ResultEntity.FAIL);
        } else {
            logger.error(req.getServletPath() + " error ", e);
            result.setMsg(e.toString());
            result.setCode(ResultEntity.FAIL);
            // 未知异常是应该重点关注的，这里可以做其他操作，如通知邮件，单独写到某个文件等等。
        }
        return result;
    }

}