package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean isNextUpSection(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isEqualsUpStation(station));
    }

    public boolean isNextDownSection(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isEqualsDownStation(station));
    }

    public Section nextUpSection(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualsUpStation(station))
                .findFirst()
                .orElse(null);
    }

    public Section nextDownSection(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualsDownStation(station))
                .findFirst()
                .orElse(null);
    }
}
