package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private List<Station> stations;
    private int distance;

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getCalculateFare() {
        int fare = 1250;

        if(distance > 10) {
            fare += (int) ((Math.ceil((distance - 10 - 1) / 5) + 1) * 100);
        }

        if(distance > 50) {
            fare += (int) ((Math.ceil((distance - 50 - 1) / 8) + 1) * 100);
        }
        return fare;
    }
}
