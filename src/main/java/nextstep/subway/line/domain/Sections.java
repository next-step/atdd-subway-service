package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {
        sections.add(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean hasNextSectionByUpStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.getUpStation() == station);
    }

    public boolean hasNextSectionByDownStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.getDownStation() == station);
    }

    public Section getNextSectionByUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.getUpStation() == station)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("다음 구간이 없습니다. (sectionId: %d)", station.getId())));
    }

    public Section getNextSectionByDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.getDownStation() == station)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("다음 구간이 없습니다. (sectionId: %d)", station.getId())));
    }
}
