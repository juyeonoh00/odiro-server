package odiro.exception.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    DUPLICATE_EMAIL_EXCEPTION(409, "이미 사용중인 이메일입니다.", BAD_REQUEST),
    DUPLICATE_NICKNAME_EXCEPTION(409, "이미 사용중인 닉네임입니다.", BAD_REQUEST),
    NOT_VERIFIED_EXCEPTION(401, "아직 인증되지 않은 이메일입니다.", UNAUTHORIZED),
    NOT_FOUND_MEMBER_EXCEPTION(404, "사용자가 존재하지 않습니다.", NOT_FOUND),
    NOT_FOUND_STUDY_EXCEPTION(404, "해당 스터디가 존재하지 않습니다.", NOT_FOUND),
    MATCH_USER_EXCEPTION(401, "해당 접근권한이 없습니다", UNAUTHORIZED),
    MATCH_POST_EXCEPTION(400, "댓글의 게시글과 일치하지 않습니다", BAD_REQUEST),
    DUPLICATE_LIKE_EXCEPTION(409, "좋아요는 1번만 가능합니다.", BAD_REQUEST),
    NOT_LIKE_EXCEPTION(409, "자신이 작성한 게시물에 좋아요 불가", BAD_REQUEST),
    NOT_WISH_EXCEPTION(409, "자신이 작성한 게시물에 찜 불가", BAD_REQUEST),
    NOT_FOUND_COMMUNITY_EXCEPTION(409, "존재하지 게시글입니다.", BAD_REQUEST),
    NOT_FOUND_COMMENT_EXCEPTION(409, "존재하지 않는 댓글입니다.", BAD_REQUEST),
    NOT_FOUND_PROJECT_EXCEPTION(404, "프로젝트가 존재하지 않습니다", NOT_FOUND),
    NOT_FOUND_PROJECT_RECRUITMENT_EXCEPTION(404, "프로젝트 모집글이 존재하지 않습니다", BAD_REQUEST);

    private final int status;
    private final String message;
    private final HttpStatus  httpStatus;
}