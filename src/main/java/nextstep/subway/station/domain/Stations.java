package nextstep.subway.station.domain;

import nextstep.subway.line.domain.Section;

import java.util.ArrayList;
import java.util.List;

public class Stations {
    private List<Station> stations;

    public Stations(List<Section> sections) {
        List<Station> stations = new ArrayList<>();
        for (int i = 0; i < sections.size() - 1; i++) {
            stations.add(sections.get(i).getUpStation());
        }
        stations.add(sections.get(sections.size() - 1).getUpStation());
        stations.add(sections.get(sections.size() - 1).getDownStation());
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

}
