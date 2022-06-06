package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Stations {
    private final List<Station> elements = new ArrayList<>();

    public void connectStation(List<Section> sections) {
        validate(sections);

        Station downStation = findUpStation(sections);
        add(downStation);

        while (downStation != null) {
            Station nextStation = findNextStation(sections, downStation);
            add(nextStation);
            downStation = nextStation;
        }
    }

    private void validate(List<Section> sections) {
        if (Objects.isNull(sections)) {
            throw new IllegalArgumentException("구간이 비어있습니다.");
        }
    }

    private Station findUpStation(List<Section> sections) {
        return sections.stream()
                .map(Section::getUpStation)
                .filter(station -> isUpStation(sections, station))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("상행역을 찾을 수 없습니다."));
    }

    private boolean isUpStation(List<Section> sections, Station station) {
        return sections.stream()
                .noneMatch(section -> section.isSameDownStation(station));
    }

    public void add(Station station) {
        if (Objects.isNull(station)) {
            return;
        }
        elements.add(station);
    }

    private Station findNextStation(List<Section> sections, Station station) {
        return sections.stream()
                .filter(section -> section.isSameUpStation(station))
                .map(Section::getDownStation)
                .findFirst()
                .orElse(null);
    }

    public List<Station> getElements() {
        return Collections.unmodifiableList(this.elements);
    }
}
