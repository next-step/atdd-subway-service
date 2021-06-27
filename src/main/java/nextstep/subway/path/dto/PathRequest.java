package nextstep.subway.path.dto;

public class PathRequest {
    private long source;
    private long target;
    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }
}
