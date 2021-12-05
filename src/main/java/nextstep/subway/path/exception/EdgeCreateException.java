package nextstep.subway.path.exception;

import nextstep.subway.common.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.common.exception.path
 * fileName : EdgeCreateException
 * author : haedoang
 * date : 2021/12/05
 * description :
 */
public class EdgeCreateException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "정점이 존재하지 않아 간선을 등록할 수 없습니다.";

    public EdgeCreateException() {
        super(status, message);
    }
}
