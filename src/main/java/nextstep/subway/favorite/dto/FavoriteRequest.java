package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private Long departureId;
    private Long arrivalId;

    public FavoriteRequest(Long departureId, Long arrivalId) {
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
