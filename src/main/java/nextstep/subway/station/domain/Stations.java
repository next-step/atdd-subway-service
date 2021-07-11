package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.common.exception.NonMatchStationException;
import nextstep.subway.common.exception.SectionsDuplicateException;
import nextstep.subway.station.dto.StationResponse;

public class Stations {

    private List<Station> stations;

    public Stations() {
        this.stations = new ArrayList<>();
    }

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    public void checkStation(Station upStation, Station downStation) {
        if(duplicateStation(upStation, downStation)) {
            throw new SectionsDuplicateException();
        }

        if (checkNonMatchStation(upStation, downStation)) {
            throw new NonMatchStationException();
        }
    }

    public boolean isMatchStation(Station station) {
        return stations.stream().anyMatch(it -> it == station);
    }

    public List<StationResponse> convert() {
        return stations.stream()
            .map(it -> StationResponse.of(it))
            .collect(Collectors.toList());
    }

    private boolean duplicateStation(Station upStation, Station downStation) {
        return isMatchStation(upStation) && isMatchStation(downStation);
    }

    private boolean checkNonMatchStation(Station upStation, Station downStation) {
        return !stations.isEmpty() && nonMatchStation(upStation) && nonMatchStation(downStation);
    }

    private boolean nonMatchStation(Station station) {
        return stations.stream().noneMatch(it -> it == station);
    }
}
