package nextstep.subway.path.dto;

public class PathRequest {
    private Long source;

    private Long target;

    public PathRequest() {
    }

    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public void validateIfSourceEqualTarget() {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public void setTarget(Long target) {
        this.target = target;
    }
}
