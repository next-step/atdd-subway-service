package nextstep.subway.line.exception.fare;

import nextstep.subway.common.exception.ServiceException;
import nextstep.subway.line.domain.ExtraCharge;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.line.exception.fare
 * fileName : IllegalFareException
 * author : haedoang
 * date : 2021/12/10
 * description :
 */
public class IllegalFareException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "요금은 %d 이상이어야 합니다.";

    public IllegalFareException() {
        super(status, String.format(message, ExtraCharge.NO_FARE));
    }
}
