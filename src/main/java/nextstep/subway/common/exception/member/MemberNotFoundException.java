package nextstep.subway.common.exception.member;

import nextstep.subway.common.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.common.exception
 * fileName : MemberNotFoundException
 * author : haedoang
 * date : 2021/12/01
 * description : 회원 미존재 예외 클래스
 */
public class MemberNotFoundException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "회원이 존재하지 않습니다.";

    public MemberNotFoundException(Long id) {
        super(status, message.concat(" memberId : " + id));
    }
}
