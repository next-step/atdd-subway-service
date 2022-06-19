package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public static Stations createUpStations(Sections sections) {
        return new Stations(sections.getSectionElements()
                .stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList()));
    }

    public static Stations createDownStations(Sections sections) {
        return new Stations(sections.getSectionElements()
                .stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList()));
    }

    public List<Station> getStationElements() {
        return Collections.unmodifiableList(stationElements);
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


}
