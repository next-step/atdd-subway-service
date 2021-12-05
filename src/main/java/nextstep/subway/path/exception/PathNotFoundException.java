package nextstep.subway.path.exception;

import nextstep.subway.common.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.path.application
 * fileName : PathNotFoundException
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
public class PathNotFoundException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "연결된 경로가 존재하지 않습니다.";

    public PathNotFoundException() {
        super(status, message);
    }
}
