package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortedStation {
    private List<Station> stations;

    public SortedStation(List<Section> sections) {
        this.stations = sort(sections);
    }

    private List<Station> sort(List<Section> sections) {
        List<Station> results = new ArrayList<>();

        SortedSection sortedSection = new SortedSection(sections);

        for (Section section : sortedSection.toCollection()) {
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
