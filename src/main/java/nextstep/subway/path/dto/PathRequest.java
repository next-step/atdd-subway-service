package nextstep.subway.path.dto;

public class PathRequest {
    private Long departureId;
    private Long arrivalId;

    public PathRequest(Long departureId, Long arrivalId) {
        this.departureId = departureId;
        this.arrivalId = arrivalId;
    }

    public Long getDepartureId() {
        return departureId;
    }

    public Long getArrivalId() {
        return arrivalId;
    }
}
