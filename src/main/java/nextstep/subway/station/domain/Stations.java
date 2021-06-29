package nextstep.subway.station.domain;

import nextstep.subway.line.domain.StationPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = new ArrayList<>(stations);
    }

    public List<StationPair> getSectionPairs() {
        List<StationPair> stationPairs = new ArrayList<>();

        for (int i = 1; i < stations.size(); i++) {
            stationPairs.add(new StationPair(stations.get(i - 1), stations.get(i)));
        }

        return stationPairs;
    }

    public List<Station> toCollection() {
        return Collections.unmodifiableList(stations);
    }
}
