package nextstep.subway.common.exception;

import nextstep.subway.common.CustomException;
import org.springframework.http.HttpStatus;

public class NoSectionDeleteException extends CustomException {
    private static final String NO_SECTION_DELETE = "등록된 구간이 없어서 삭제할 수 없습니다.";

    public NoSectionDeleteException() {
        super(HttpStatus.BAD_REQUEST, NO_SECTION_DELETE);
    }
}
