package nextstep.subway.station.exception;

import nextstep.subway.common.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.common.exception
 * fileName : StationNotFoundException
 * author : haedoang
 * date : 2021/12/01
 * description : 역 없음 예외 클래스
 */
public class StationNotFoundException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "역이 존재하지 않습니다.";

    public StationNotFoundException() {
        super(status, message);
    }
}
