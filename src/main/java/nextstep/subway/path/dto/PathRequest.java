package nextstep.subway.path.dto;

public class PathRequest {
    private Long sourceStationId;
    private Long targetStationId;

    public PathRequest(Long sourceStationId, Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    @Override
    public String toString() {
        return "PathRequest{" +
                "sourceStationId=" + sourceStationId +
                ", targetStationId=" + targetStationId +
                '}';
    }
}
