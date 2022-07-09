package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.charge.domain.Charge;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class Path {
    private final Stations stations;
    private final int distance;
    private Charge charge;

    public Path(List<Station> stations, int distance) {
        this.stations = new Stations(stations);
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStationResponses() {
        return stations.toStationResponses();
    }

    public void updateCharge(Charge charge) {
        this.charge = charge;
    }

    public List<Section> getPathRouteSections(List<Section> allSections) {
        return stations.getRouteSections(allSections);
    }

    public int getFare() {
        return charge.getChargeValue();
    }
}
