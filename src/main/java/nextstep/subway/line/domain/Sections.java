package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.common.exception.SectionsRemovalInValidSizeException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    static final private int REMOVE_MINIMUM_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void validateRemoveSize() {
        if(sections.size() <= REMOVE_MINIMUM_SIZE) {
            throw new SectionsRemovalInValidSizeException();
        }
    }

    public void add(Section section) {
        sections.add(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Station findFirstUpStation() {
        if(sections.isEmpty()) {
            return null;
        }
        return this.sections.get(0).getUpStation();
    }

    public Section findSectionByDownStation(Station station) {
        return sections.stream()
            .filter(it -> it.getDownStation().equals(station))
            .findFirst()
            .orElse(null);
    }

    public Section findSectionByUpStation(Station station) {
        return sections.stream()
            .filter(it -> it.getUpStation().equals(station))
            .findFirst()
            .orElse(null);
    }

    public void removeSectionByStation(Station station, Line line) {
        Section upLineStation = findSectionByUpStation(station);
        Section downLineStation = findSectionByDownStation(station);

        if (upLineStation != null && downLineStation != null) {
            Station newUpStation = downLineStation.getUpStation();
            Station newDownStation = upLineStation.getDownStation();
            int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        if(upLineStation != null) {
            sections.remove(upLineStation);
        }

        if(downLineStation != null) {
            sections.remove(downLineStation);
        }
    }
}
