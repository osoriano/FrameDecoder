package com.osoriano.framedecoder;

public class InvalidFrameException extends Exception {

    public InvalidFrameException() { }

    public InvalidFrameException(String message) {
        super(message);
    }

    public InvalidFrameException(Throwable cause) {
        super(cause);
    }

    public InvalidFrameException(String message, Throwable cause) {
        super(message, cause);
    }

}
