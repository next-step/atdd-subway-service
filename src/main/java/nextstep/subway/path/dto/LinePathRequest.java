package nextstep.subway.path.dto;


public class LinePathRequest {
    private Long source;
    private Long target;

    public LinePathRequest() {
    }

    public LinePathRequest(Long source, Long target) {
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
