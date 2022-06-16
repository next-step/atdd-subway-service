package nextstep.subway.path.dto;

public class PathRequest {
    private int source;
    private int target;

    public PathRequest(int source, int target) {
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
