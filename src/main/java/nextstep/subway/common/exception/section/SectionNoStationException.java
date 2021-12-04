package nextstep.subway.common.exception.section;

import nextstep.subway.common.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.common.exception
 * fileName : SectionNoStationException
 * author : haedoang
 * date : 2021/12/01
 * description : 구간 생성 예외 클래스
 */
public class SectionNoStationException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "등록할 수 없는 구간 입니다.";

    public SectionNoStationException() {
        super(status, message);
    }
}
