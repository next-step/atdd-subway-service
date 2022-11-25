package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections;

    public Sections()  {
        this.sections = new ArrayList<>();
    }

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

    public List<Section> value() {
        return sections;
    }

    public void add(final Section section) {
        sections.add(section);
    }
}
