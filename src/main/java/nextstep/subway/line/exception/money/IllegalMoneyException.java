package nextstep.subway.line.exception.money;

import nextstep.subway.common.exception.ServiceException;
import nextstep.subway.line.domain.Money;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : IllegalMoneyException
 * author : haedoang
 * date : 2021/12/12
 * description :
 */
public class IllegalMoneyException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "금액은 %d원 이상이어야 합니다.";

    public IllegalMoneyException() {
        super(status, String.format(message, Money.MIN_VALUE));
    }
}
