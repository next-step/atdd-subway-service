package nextstep.subway.path.dto;

public class PathRequest {

    private Long source;
    private Long target;

    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSourceId() {
        return this.source;
    }

    public Long getTargetId() {
        return this.target;
    }

}
