package nextstep.subway.path.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PathException extends RuntimeException {
    public static final String PATH_FIND_NO_SEARCH_MSG = "최단 거리 조회 시, 조회 하려는 역 사이가 연결되어 있지 않습니다.";
    public static final String SAME_SOURCE_TARGET_STATION_MSG = "출발역과 도착역이 같은 경우 최단거리를 조회 할 수 없습니다.";
    public static final String NO_CONTAIN_STATION_MSG = "조회하려는 역이 존재하지 않습니다.";

    public PathException(String msg) {
        super(msg);
    }
}
