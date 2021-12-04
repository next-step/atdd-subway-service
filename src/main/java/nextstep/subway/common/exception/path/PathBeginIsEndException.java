package nextstep.subway.common.exception.path;

import nextstep.subway.common.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.common.exception.path
 * fileName : PathBeginIsEndException
 * author : haedoang
 * date : 2021/12/04
 * description : 경로 출발지,목적지 설정 예외
 */
public class PathBeginIsEndException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "출발지와 목적지가 같습니다.";

    public PathBeginIsEndException(Long sourceId, Long targetId) {
        super(status, new StringBuilder().append(message)
                .append(" srcId: ")
                .append(sourceId)
                .append(" targetId: ")
                .append(targetId)
                .toString()
        );
    }
}
