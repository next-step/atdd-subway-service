package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SortedStations {
    private List<Station> stations;

    public SortedStations(List<Section> sections) {
        this.stations = sort(sections);
    }

    public List<StationResponse> toResponses() {
        return this.stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    private List<Station> sort(List<Section> sections) {
        List<Station> results = new ArrayList<>();

        SortedSections sortedSections = new SortedSections(sections);

        for (Section section : sortedSections.toCollection()) {
            addStation(results, section);
        }

        return results;
    }

    private void addStation(List<Station> stations, Section section) {
        if (stations.isEmpty()) {
            stations.add(section.getUpStation());
        }

        stations.add(section.getDownStation());
    }

    protected List<Station> toCollection() {
        return Collections.unmodifiableList(stations);
    }
}
