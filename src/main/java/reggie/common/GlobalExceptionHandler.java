package reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 */
@Slf4j
@ResponseBody
//会对加上RestController和Controller的注解进行异常处理操作
@ControllerAdvice(annotations={RestController.class, Controller.class})
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandle(SQLIntegrityConstraintViolationException ex){
        //当输入的用户名重复时
         if(ex.getMessage().contains("Duplicate entry")){
             String[] s = ex.getMessage().split(" ");
             String msg=s[2]+"已经存在";
             return  R.error(msg);
         }
          return  R.error("未知错误");
    }
    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}

