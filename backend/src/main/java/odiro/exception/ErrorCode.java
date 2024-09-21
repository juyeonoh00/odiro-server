package odiro.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public enum ErrorCode {
    // 토큰이 없으면 401
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다"),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "잘못된 토큰 형식입니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰 형식입니다"),
    CANNOT_CREATE_CODE(HttpStatus.UNPROCESSABLE_ENTITY, "코드를 생성할 수 없습니다"),


    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "유저 이름이 중복됩니다"),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이메일이 중복됩니다"),
    USER_NOT_FOUNDED(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "bad Request");

    private final HttpStatus httpStatus;
    private final String message;

}