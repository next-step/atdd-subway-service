package nextstep.subway.line.domain;

import nextstep.subway.exception.RemoveSectionFail;
import nextstep.subway.exception.AddLineSectionFail;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {

    }

    public void removeLineStation(Section section) {
        sections.remove(section);
    }

    public void addLineStation(Section section) {
        sections.add(section);
    }

    public void addDownStationExisted(Section section) {
        sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

        addLineStation(section);
    }

    public void addUpStationExisted(Section section) {
        sections.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

        addLineStation(section);
    }

    public List<Section> getSections() {
        return sections;
    }
}
