package com.cft.focusstart.library.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.cft.focusstart.library.log.messages.ServiceLogMessages.SERVICE_LOG_EXCEPTION;

@Slf4j
public class ServiceException extends RuntimeException {

    public static final String SERVICE_EXCEPTION_SERVICE_NAME_DEFAULT_VALUE = "service not set";
    public static final String SERVICE_EXCEPTION_FROM_ANOTHER_EXCEPTION_FORMAT_STRING = "exception %s with message %s";
    public static final String SERVICE_EXCEPTION_NO_ENTITY_WITH_ID_FORMAT_STRING = "no entity with id %d";
    public static final String SERVICE_EXCEPTION_ENTITY_WITH_RELATED_FIELD_FORMAT_STRING = "entity have related field with name %s";
    public static final String SERVICE_EXCEPTION_WRONG_ENTITY_FORMAT_STRING = "save/update wrong entity: %s";
    public static final String SERVICE_EXCEPTION_EXIST_ENTITY_FORMAT_STRING = "save/update exist entity";
    public static final String SERVICE_EXCEPTION_SET_WRONG_SUB_ENTITY_FORMAT_STRING = "can't set sub entity with id = %d and wrong field %s = %s";

    @Getter
    private String serviceName = SERVICE_EXCEPTION_SERVICE_NAME_DEFAULT_VALUE;

    private ServiceException(String serviceName, String message) {
        super(message);
        if(serviceName != null) {
            this.serviceName = serviceName;
        }
        log.error(SERVICE_LOG_EXCEPTION, serviceName, message);
    }

    public static ServiceException serviceExceptionFromAnotherException(String service, String exceptionName, String exceptionMessage) {
        return new ServiceException(
                    service,
                    String.format(
                            SERVICE_EXCEPTION_FROM_ANOTHER_EXCEPTION_FORMAT_STRING,
                            exceptionName,
                            exceptionMessage
                    )
            );
    }

    public static ServiceException serviceExceptionWrongEntity(String service, String message) {
        return new ServiceException(
                service,
                String.format(
                        SERVICE_EXCEPTION_WRONG_ENTITY_FORMAT_STRING,
                        message
                )
        );
    }

    public static ServiceException serviceExceptionExistEntity(String service) {
        return new ServiceException(service, SERVICE_EXCEPTION_EXIST_ENTITY_FORMAT_STRING);
    }

    public static ServiceException serviceExceptionNoEntityWithId(String service, Long id) {
        return new ServiceException(
                service,
                String.format(
                        SERVICE_EXCEPTION_NO_ENTITY_WITH_ID_FORMAT_STRING,
                        id
                )
        );
    }

    public static ServiceException serviceExceptionDeleteOrUpdateRelatedEntity(String service, String relatedFieldName) {
        return new ServiceException(
                service,
                String.format(
                        SERVICE_EXCEPTION_ENTITY_WITH_RELATED_FIELD_FORMAT_STRING,
                        relatedFieldName
                )
        );
    }

    public static ServiceException serviceExceptionSetWrongSubEntity(String service, Long id, String wrongField, String wrongStatus) {
        return new ServiceException(
                service,
                String.format(
                        SERVICE_EXCEPTION_SET_WRONG_SUB_ENTITY_FORMAT_STRING,
                        id,
                        wrongField,
                        wrongStatus
                )
        );
    }
}
