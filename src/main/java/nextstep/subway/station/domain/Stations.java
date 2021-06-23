package nextstep.subway.station.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SimpleSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = new ArrayList<>(stations);
    }

    public List<SimpleSection> getSimpleSection() {
        List<SimpleSection> simpleSections = new ArrayList<>();

        for (int i = 1; i <stations.size(); i++) {
            simpleSections.add(new SimpleSection(stations.get(i - 1), stations.get(i)));
        }

        return simpleSections;
    }

    public List<Station> toCollection() {
        return Collections.unmodifiableList(stations);
    }
}
