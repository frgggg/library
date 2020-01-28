package com.cft.focusstart.library.exception;

import lombok.extern.slf4j.Slf4j;

import static com.cft.focusstart.library.log.messages.ServiceExceptionLogMessages.SERVICE_EXCEPTION_LOG;

@Slf4j
public class ServiceException extends RuntimeException {

    public static final String DELETE_BY_ID_EXCEPTION_MESSAGE = "can't delete entity: ";
    public static final String FIND_BY_ID_EXCEPTION_MESSAGE = "no entity with id = ";
    public static final String SAVE_EXCEPTION_MESSAGE = "can't save exist entity: ";
    public static final String UPDATE_EXCEPTION_MESSAGE = "can't update entity: ";

    public static final String UNKNOWN_EXCEPTION_MESSAGE = "unknown error";
    public static final String DEFAULT_REASON_EXCEPTION_MESSAGE = "no reason exception";

    private String reasonExceptionMessage = DEFAULT_REASON_EXCEPTION_MESSAGE;

    public ServiceException(String service, String msg, Exception e) {
        super(service + ": " + msg);
        if(e != null) {
            reasonExceptionMessage = e.getMessage();
        }
        log.error(SERVICE_EXCEPTION_LOG, service, msg, reasonExceptionMessage);
    }

    public String getReasonExceptionMessage() {
        return reasonExceptionMessage;
    }

    public static ServiceException getDeleteByIdServiceException(String service, String entityToString) {
        return new ServiceException(service, DELETE_BY_ID_EXCEPTION_MESSAGE + entityToString, null);
    }

    public static ServiceException getFindByIdServiceException(String service, Long id) {
        return new ServiceException(service, FIND_BY_ID_EXCEPTION_MESSAGE + id, null);
    }

    public static ServiceException getSaveServiceException(String service, String entityToString, Exception reasonException) {
        return new ServiceException(service, SAVE_EXCEPTION_MESSAGE + entityToString, reasonException);
    }

    public static ServiceException getUpdateServiceException(String service, String entityToString, Exception reasonException) {
        return new ServiceException(service, UPDATE_EXCEPTION_MESSAGE + entityToString, reasonException);
    }

    public static ServiceException getUnknownServiceException(String service, Exception reasonException) {
        return new ServiceException(service, UNKNOWN_EXCEPTION_MESSAGE, reasonException);
    }
}
