package nextstep.subway.path.dto;

public class PathRequest {
    private Integer source;
    private Integer target;

    public PathRequest(Integer source, Integer target) {
        this.source = source;
        this.target = target;
    }

    public Integer getSource() {
        return source;
    }

    public Integer getTarget() {
        return target;
    }
}
