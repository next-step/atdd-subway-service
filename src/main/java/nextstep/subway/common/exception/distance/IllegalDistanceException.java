package nextstep.subway.common.exception.distance;

import nextstep.subway.common.exception.ServiceException;
import nextstep.subway.line.domain.Distance;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.common.exception.distance
 * fileName : IllegalDistanceException
 * author : haedoang
 * date : 2021-12-02
 * description : Distance 객체 유효성 검증 예외
 */
public class IllegalDistanceException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "거리는 %d 이상이어야 합니다.";

    public IllegalDistanceException() {
        super(status, String.format(message, Distance.MIN_DISTANCE));
    }
}
