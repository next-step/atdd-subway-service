package nextstep.subway.path.dto;

public class PathRequest {
    private long source;
    private long target;

    private PathRequest() {
    }

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
}