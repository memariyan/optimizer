package com.memariyan.optimizer.presentation.msg.exception;

import com.memariyan.components.common.exception.ValidationException;
import lombok.Getter;

import java.util.List;

@Getter
public class InvalidRequestException extends ValidationException {

    private final List<String> errorMessages;

    public InvalidRequestException(String message, List<String> errorMessages) {
        super(message);
        this.errorMessages = errorMessages;
    }

}
