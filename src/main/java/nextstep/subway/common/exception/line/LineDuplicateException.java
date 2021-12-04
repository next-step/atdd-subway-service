package nextstep.subway.common.exception.line;

import nextstep.subway.common.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.common.exception
 * fileName : LineDuplicateException
 * author : haedoang
 * date : 2021/12/01
 * description : 중복 라인 예외처리
 */
public class LineDuplicateException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "중복된 노선이 존재합니다.";

    public LineDuplicateException() {
        super(status, message);
    }
}
