package nextstep.subway.path.dto;

import java.util.Objects;

public class PathRequest {
    private final static long INIT_PATH_STATION_ID = 0L;

    private long source;
    private long target;

    public PathRequest(long source, long target) {
        this.source = source;
        this.target = target;
    }

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }

    public static void validate(PathRequest pathRequest) {
        if (Objects.isNull(pathRequest)) {
            throw new IllegalArgumentException("요청한 경로가 존재하지 않습니다.");
        }
        if (isInitPathSource(pathRequest)) {
            throw new IllegalArgumentException("source 값은 유효하지 않습니다");
        }
        if (isInitPathTarget(pathRequest)) {
            throw new IllegalArgumentException("target 값은 유효하지 않습니다");
        }
    }

    private static boolean isInitPathTarget(PathRequest pathRequest) {
        return pathRequest.getTarget() == INIT_PATH_STATION_ID;
    }

    private static boolean isInitPathSource(PathRequest pathRequest) {
        return pathRequest.getSource() == INIT_PATH_STATION_ID;
    }


}
