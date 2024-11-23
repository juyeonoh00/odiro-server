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
    CANNOT_CREATE_CODE(HttpStatus.UNPROCESSABLE_ENTITY, "코드를 생성할 수 없습니다"),


    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "유저 이름이 중복됩니다"),
    NOT_AUTHERIZED_USER(HttpStatus.CONFLICT, "권한이 없는 유저입니다"),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이메일이 중복됩니다"),


    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 입니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "유효하지 않은 비밀번호입니다"),
    INVALID_PLAN_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 Plan Id 입니다"),
    INVALID_DAY_PLAN_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 Day Plan Id 입니다"),
    INVALID_COMMENT_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 Comment Id 입니다"),

    FILE_IS_EMPTY(HttpStatus.NOT_FOUND, "file이 비어있습니다"),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "해당 토큰을 찾을 수 없습니다"),
    USER_NOT_FOUNDED(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"),
    DAYPLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "Day Plan을 찾을 수 없습니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment를 찾을 수 없습니다");



    private final HttpStatus httpStatus;
    private final String message;

}