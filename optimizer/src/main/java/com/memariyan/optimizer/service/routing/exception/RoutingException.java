package com.memariyan.optimizer.service.routing.exception;

import com.memariyan.components.common.exception.BusinessException;
import com.memariyan.optimizer.api.msg.dto.ErrorCodes;

public class RoutingException extends BusinessException {

    public RoutingException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.ROUTING_ERROR;
    }

}
