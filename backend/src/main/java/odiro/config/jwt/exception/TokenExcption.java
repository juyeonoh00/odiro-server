package odiro.config.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenExcption extends RuntimeException {
    private final ErrorCode errorCode;
}
