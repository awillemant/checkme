package io.checkme.exception;

public class RouteFormatException extends Exception {

    private static final long serialVersionUID = -6591879179078859572L;

    private final String key;

    private final String message;


    public RouteFormatException(String message, String key) {
        this.message = message;
        this.key = key;
    }


    @Override
    public String getMessage() {
        return message + " : " + key;
    }
}
