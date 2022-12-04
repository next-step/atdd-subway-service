package nextstep.subway.path.ui;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse(final List<StationResponse> stations, final int distance, final int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    protected PathResponse() {
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getAdditionalFare() {
        return fare;
    }

    public void applyDiscountFare(final int age) {
        if (isChild(age)) {
            applyChildDiscountFare();
            return;
        }

        if (isTeen(age)) {
            applyTeenDiscountFare();
        }
    }

    private boolean isChild(int age) {
        return age >= 6 && age < 13;
    }

    private boolean isTeen(int age) {
        return age >= 13 && age < 19;
    }

    private void applyChildDiscountFare() {
        fare = fare - ((fare - 350) / 5);
    }

    private void applyTeenDiscountFare() {
        fare = fare - ((fare - 350) / 2);
    }
}
