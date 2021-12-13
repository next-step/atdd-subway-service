package nextstep.subway.path.dto;

public class PathRequest {

    private Long source;
    private Long target;

    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public static PathRequest of(Long source, Long target) {
        return new PathRequest(source, target);
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
