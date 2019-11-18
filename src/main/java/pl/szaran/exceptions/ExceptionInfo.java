package pl.szaran.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionInfo {
    private String exceptionMessage;
    private LocalDateTime exceptionDateTime;

    public ExceptionInfo(String exceptionMessage, LocalDateTime exceptionDateTime) {
        this.exceptionMessage = exceptionMessage;
        this.exceptionDateTime = exceptionDateTime;
    }

    @Override
    public String toString() {
        return "ExceptionInfo: \n" +
                "\t Message: " + exceptionMessage + '\n' +
                "\t Time: " + exceptionDateTime;
    }
}
