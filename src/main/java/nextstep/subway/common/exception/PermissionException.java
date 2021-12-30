package nextstep.subway.common.exception;

import nextstep.subway.common.CustomException;
import org.springframework.http.HttpStatus;

public class PermissionException extends CustomException {
    private static final String NO_PERMISSION = "해당 요청은 권한이 없습니다.";

    public PermissionException() {
        super(HttpStatus.BAD_REQUEST, NO_PERMISSION);
    }
}
