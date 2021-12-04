package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void add(Section newSection) {
        this.sections.add(newSection);
    }

    public List<Section> getSections() {
        List<Section> sortedSections = new ArrayList<>();
        Section section = findFirstSection();
        sortedSections.add(section);

        while (sortedSections.size() < this.sections.size()) {
            section = findNextStation(section.getDownStation());
            sortedSections.add(section);
        }

        return Collections.unmodifiableList(sortedSections);
    }

    public Section findFirstSection() {
        List<Station> downStations = getDownStations();

        return sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("상행 종점역을 찾을 수 없습니다."));
    }

    private Section findNextStation(Station downStation) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(downStation))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("이어지는 구간을 찾을 수 없습니다."));
    }

    public List<Station> getDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void remove(Section section) {
        sections.remove(section);
    }
}
