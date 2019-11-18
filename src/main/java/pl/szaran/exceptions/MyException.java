package pl.szaran.exceptions;

public class MyException extends RuntimeException{
    private String exceptionMessage;

    public MyException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
