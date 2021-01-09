package nextstep.subway.fare.dto;

public class FareResponse {
    private final int fare;

    public FareResponse(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }
}
