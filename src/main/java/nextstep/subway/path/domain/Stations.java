package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class Stations {
    public static final String ERROR_MESSAGE_STATIONS_NULL = "역 목록이 null 입니다.";
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        validateStations(stations);
        this.stations = stations;
    }

    private void validateStations(List<Station> stations) {
        if(stations == null) {
            throw new NullPointerException(ERROR_MESSAGE_STATIONS_NULL);
        }
    }

    public List<StationResponse> toStationResponses() {
        return stations.stream().map(StationResponse::of).collect(Collectors.toList());
    }

    public List<Section> getRouteSections(List<Section> allSections) {
        List<Section> result = new ArrayList<>();

        for (int i = 0; i < stations.size() - 1; i++) {
            Section section = findSection(allSections, stations.get(i), stations.get(i + 1));
            result.add(section);
        }

        return result;
    }

    private Section findSection(List<Section> allSections, Station upStation, Station downStation) {
        return allSections.stream()
                .filter(section -> section.hasStation(upStation) && section.hasStation(downStation))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
