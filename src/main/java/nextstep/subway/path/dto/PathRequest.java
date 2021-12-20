package nextstep.subway.path.dto;

@DifferentSourceAndTarget(message = "출발역과 도착역이 같습니다.")
public class PathRequest {
    private long source;
    private long target;

    public PathRequest(long source, long target) {
        this.source = source;
        this.target = target;
    }

    public boolean isDifferentSourceAndTarget() {
        return source != target;
    }

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }
}
