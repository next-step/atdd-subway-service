package nextstep.subway.common.exception.distance;

import nextstep.subway.common.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.common.exception.distance
 * fileName : InvalidDistanceException
 * author : haedoang
 * date : 2021-12-02
 * description : Distance 구간 사이 거리 예외 클래스
 */
public class DistanceNotAllowException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    public DistanceNotAllowException() {
        super(status, message);
    }
}
