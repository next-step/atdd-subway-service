package nextstep.subway.path.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidFarePolicyException extends RuntimeException {

    public static final String MESSAGE = "계산할 수 없는 요금 입력입니다";

    public InvalidFarePolicyException() {
        super(MESSAGE);
    }
}
