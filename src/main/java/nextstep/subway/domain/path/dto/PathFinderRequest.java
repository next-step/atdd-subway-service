package nextstep.subway.domain.path.dto;

public class PathFinderRequest {

    private Long source;
    private Long target;

    public PathFinderRequest(final Long source, final Long target) {
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
