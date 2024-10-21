package odiro.config.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor

public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "user name is duplicated"),
    USER_NOT_FOUNDED(HttpStatus.NOT_FOUND, "not found error"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "bad Request"),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "Token not found");

    private final HttpStatus httpStatus;
    private final String message;
}