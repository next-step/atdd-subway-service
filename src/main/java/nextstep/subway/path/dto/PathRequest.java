package nextstep.subway.path.dto;

public class PathRequest {
    private long source;
    private long target;

    public PathRequest() {
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

    public void setSource(long source) {
        this.source = source;
    }

    public void setTarget(long target) {
        this.target = target;
    }
}
