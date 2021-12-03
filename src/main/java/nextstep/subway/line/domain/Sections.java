package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    protected Sections () {
        this.sections = new ArrayList<>();
    }

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public List<Station> getStations() {
        return this.sections.stream()
            .sorted()
            .map(section -> section.getUpDownStations())
            .flatMap(stations -> stations.stream())
            .distinct()
            .collect(Collectors.toList());
    }
}
