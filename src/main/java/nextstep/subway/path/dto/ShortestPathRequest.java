package nextstep.subway.path.dto;

public class ShortestPathRequest {
    private Long startingStationId;
    private Long destinationStationId;

    public ShortestPathRequest() {
    }

    public ShortestPathRequest(Long startingStationId, Long destinationStationId) {
        this.startingStationId = startingStationId;
        this.destinationStationId = destinationStationId;
    }

    public Long getStartingStationsId() {
        return startingStationId;
    }

    public Long getDestinationStationsId() {
        return destinationStationId;
    }
}
