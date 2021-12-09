package nextstep.subway.domain.path.dto;

public class PathFinderRequest {

    private int source;
    private int target;

    public PathFinderRequest(final int source, final int target) {
        this.source = source;
        this.target = target;
    }

    public int getSource() {
        return source;
    }

    public int getTarget() {
        return target;
    }
}
