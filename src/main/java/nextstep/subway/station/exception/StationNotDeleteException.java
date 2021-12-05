package nextstep.subway.station.exception;

import nextstep.subway.common.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.common.exception.station
 * fileName : StationNotDeleteException
 * author : haedoang
 * date : 2021-12-02
 * description : 역 삭제 예외 클래스
 */
public class StationNotDeleteException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "역을 삭제할 수 없습니다.";

    public StationNotDeleteException() {
        super(status, message);
    }
}
