package nextstep.subway.path.dto;

public class ShortestPathRequest {
    private Long startingStationsId;
    private Long destinationStationsId;

    public Long getStartingStationsId() {
        return startingStationsId;
    }

    public Long getDestinationStationsId() {
        return destinationStationsId;
    }
}
