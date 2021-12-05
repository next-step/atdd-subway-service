package nextstep.subway.station.exception;

import nextstep.subway.common.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.common.exception.station
 * fileName : StationDuplicateException
 * author : haedoang
 * date : 2021-12-02
 * description : 역 중복 관련예외 클래스
 */
public class StationDuplicateException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "중복된 역이 존재합니다.";

    public StationDuplicateException() {
        super(status, message);
    }
}
