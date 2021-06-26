package nextstep.subway.path.dto;

public class PathRequest {
    private Long source;
    private Long target;

    public PathRequest() {
        // empty;
    }

    public PathRequest(final Long source, final Long target) {
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
