package nextstep.subway.path.dto;

public class PathRequest {
    private Long sourceStationId;
    private Long targetStationId;

    private PathRequest(Long sourceStationId, Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public static PathRequest of(Long sourceStationId, Long targetStationId) {
        return new PathRequest(sourceStationId, targetStationId);
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
