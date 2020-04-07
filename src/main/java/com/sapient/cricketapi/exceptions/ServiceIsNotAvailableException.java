package com.sapient.cricketapi.exceptions;

public class ServiceIsNotAvailableException extends RuntimeException {
    public ServiceIsNotAvailableException(String msg) {
        super(msg);
    }
}
