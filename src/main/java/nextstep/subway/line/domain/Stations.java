package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class Stations {

    private List<Station> stationElements = new ArrayList<>();

    private Stations(List<Station> stationElements) {
        this.stationElements = stationElements;
    }

    public static Stations of(List<Station> stationElements) {
        return new Stations(stationElements);
    }


    public Optional<Station> isNotContainsFirstStation(Stations target) {
        return this.stationElements.stream()
                .filter((target::isNotContains))
                .findFirst();
    }

    public List<StationResponse> mapToResponse() {
        return stationElements.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    private boolean isNotContains(Station station) {
        return !stationElements.contains(station);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stations stations = (Stations) o;
        return Objects.equals(stationElements, stations.stationElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationElements);
    }
}
