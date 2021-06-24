package nextstep.subway.path.dto;

public class PathRequest {
    private static final Long STATION_ID_MIN_VALUE = 1L;
    private Long source;
    private Long target;

    public PathRequest() {
    }

    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
