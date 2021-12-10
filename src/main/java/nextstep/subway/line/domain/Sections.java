package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final int SECTIONS_START_AT = 0;

    @OneToMany(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        mappedBy = "line",
        orphanRemoval = true
    )
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void add(final Section newSection) {
        sections.add(newSection);
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStationsInOrder() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        final Section firstSection = getFirstSection();
        final List<Station> stationsInOrder = new ArrayList<>(
            Arrays.asList(firstSection.getUpStation())
        );
        Section it = firstSection;
        while (it != null) {
            final Section tmp = it;
            it = sections.stream()
                .filter(s -> Objects.equals(s.getUpStation(), tmp.getDownStation()))
                .findFirst()
                .orElse(null);
            stationsInOrder.add(tmp.getDownStation());
        }
        return stationsInOrder;
    }

    private Section getFirstSection() {
        final Station firstStation = getFirstStation();
        return sections.stream()
            .filter(s -> Objects.equals(s.getUpStation(), firstStation))
            .findFirst()
            .orElseThrow(IllegalStateException::new);
    }

    private Station getFirstStation() {
        final Set<Station> upStations = sections.stream()
            .map(Section::getUpStation)
            .collect(Collectors.toSet());
        final Set<Station> downStations = sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toSet());
        upStations.removeAll(downStations);
        return new ArrayList<>(upStations).get(SECTIONS_START_AT);
    }
}
