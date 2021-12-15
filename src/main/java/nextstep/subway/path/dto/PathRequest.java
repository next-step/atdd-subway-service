package nextstep.subway.path.dto;

public class PathRequest {
    private Long srcStationId;
    private Long destStationId;

    public PathRequest() {
    }

    public PathRequest(Long srcStationId, Long destStationId) {
        this.srcStationId = srcStationId;
        this.destStationId = destStationId;
    }

    public Long getSrcStationId() {
        return srcStationId;
    }

    public Long getDestStationId() {
        return destStationId;
    }
}
