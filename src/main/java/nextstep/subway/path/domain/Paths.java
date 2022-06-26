package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Paths {
    private final List<Path> elements = new ArrayList<>();

    public Paths(List<Station> stations) {
        for (int i = 0; i < stations.size() - 1; i++) {
            elements.add(new Path(stations.get(i), stations.get(i + 1)));
        }
    }

    public boolean contains(Section section) {
        return elements.stream()
                .anyMatch(path -> path.isSame(section.getUpStation(), section.getDownStation()));
    }
}
