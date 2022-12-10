package nextstep.subway.station.domain;

import java.util.List;

public class StationLineUp {
    private final List<Station> stationList;

    public StationLineUp(List<Station> stationList) {
        this.stationList = stationList;
    }

    public boolean stationExisted(Station station) {
        return this.stationList.stream().anyMatch(it -> it == station);
    }


    public boolean isEmpty() {
        return this.stationList.isEmpty();
    }

    public boolean unKnownStation(Station station) {
        return this.stationList.stream().noneMatch(it -> it.equals(station));
    }

    public boolean hasStation(Station station) {
        return this.stationList.stream().anyMatch(it -> it.equals(station));
    }
}
