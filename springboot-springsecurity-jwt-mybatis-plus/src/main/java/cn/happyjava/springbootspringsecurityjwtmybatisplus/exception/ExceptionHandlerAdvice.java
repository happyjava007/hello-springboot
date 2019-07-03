package cn.happyjava.springbootspringsecurityjwtmybatisplus.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Happy
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * 权限不足
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity handleInsufficientAuthenticationException(InsufficientAuthenticationException e,
                                                                    HttpServletRequest request) {
        // TODO 处理未登录的请求
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
    }

}
