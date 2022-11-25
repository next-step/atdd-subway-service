package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.*;
import java.util.stream.Collectors;

public class Sections {
    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        return sections.stream()
                .map(o -> Arrays.asList(o.getUpStation(), o.getDownStation()))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

    }
}
