package com.vilgodskiy.modshare.application.config.api;

import com.elementsoft.common.exception.ValidationException;
import com.google.common.base.Throwables;
import com.vilgodskiy.modshare.application.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ErrorMessage> handleException(Exception ex) {
        logger.error("Серверная ошибка", ex);
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.addError(ex.getLocalizedMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler(value = {ExecutionConflictException.class})
    protected ResponseEntity<ErrorMessage> handleExecutionConflictException(ExecutionConflictException ex) {
        return ResponseEntity.status(CONFLICT).body(ex.getErrMsg());
    }

    @ExceptionHandler(value = {ValidationException.class})
    protected ResponseEntity<ErrorMessage> handleValidationException(ValidationException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.addErrors(ex.getErrors());
        return ResponseEntity.status(CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(value = {NotFoundObjectException.class})
    protected ResponseEntity<ErrorMessage> handleNotFoundObjectException(NotFoundObjectException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.addError(ex.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.addError(ex.getMessage());
        return ResponseEntity.status(FORBIDDEN).body(errorMessage);
    }

    @ExceptionHandler(value = {AuthAccessDeniedException.class})
    protected ResponseEntity<ErrorMessage> handleAuthAccessDeniedException(AuthAccessDeniedException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.addError(ex.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).body(errorMessage);
    }

    @ExceptionHandler(value = {AccessObjectException.class})
    protected ResponseEntity<ErrorMessage> handleAccessObjectException(AccessObjectException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.addError(ex.getMessage());
        return ResponseEntity.status(FORBIDDEN).body(errorMessage);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<ErrorMessage> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.addError("Ошибка запроса: " + ex.getMessage());
        return ResponseEntity.status(CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    protected ResponseEntity<ErrorMessage> handleMaxUploadSizeExceedException(MaxUploadSizeExceededException e) {
        Throwable throwable = Throwables.getRootCause(e);
        ErrorMessage errorMessage = new ErrorMessage();
        if (throwable instanceof FileSizeLimitExceededException) {
            FileSizeLimitExceededException exception = (FileSizeLimitExceededException) throwable;
            errorMessage.addError("Превышен размер файла " + exception.getFileName() +
                    ". Максимальный размер составляет " + exception.getPermittedSize() + " bytes");
        } else if (throwable instanceof SizeLimitExceededException) {
            SizeLimitExceededException exception = (SizeLimitExceededException) throwable;
            errorMessage.addError("Превышен общий объем вложений. Максимальный объем составляет " +
                    exception.getPermittedSize() + " bytes");
        }
        return ResponseEntity.status(CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(value = {ClientAbortException.class})
    protected void handleClientAbortedException(ClientAbortException ex) {
        // Ничего не делаем. Клиент разорвал соединение или перезагрузил страницу не дождавшись ответа.
        // Не логируем. Засирает лог на проде.
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.addError("Ошибка парсинга тела запроса: " + ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(errorMessage);
    }
}