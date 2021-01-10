package nextstep.subway.path.dto;

public class PathRequest {
    private final long source;
    private final long target;

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
