package nextstep.subway.path.dto;

public class PathRequest {
    private Long sourceId;
    private Long targetId;

    public PathRequest() {
    }

    public PathRequest(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }
}
