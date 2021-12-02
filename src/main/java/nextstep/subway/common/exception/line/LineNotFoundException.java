package nextstep.subway.common.exception.line;

import nextstep.subway.common.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.common.exception
 * fileName : LineNotFoundException
 * author : haedoang
 * date : 2021/12/01
 * description : 노선 없음 Custom Exception
 */
public class LineNotFoundException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "노선이 존재하지 않습니다.";

    public LineNotFoundException() {
        super(status, message);
    }
}
