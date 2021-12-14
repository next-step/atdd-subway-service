package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() { //오류방지
        return null;
    }

    public List<Station> getSortedStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(findAllStation());
    }

    private List<Station> findAllStation() {
        Section firstSection = findFirstSection();
        List<Station> stations = new ArrayList<>(
            Arrays.asList(firstSection.getUpStation(), firstSection.getDownStation())
        );

        List<Station> upStations = upStationsOfSections();
        Section lastSection = firstSection;
        while (upStations.contains(lastSection.getDownStation())) {
            Station finalDownStation = lastSection.getDownStation();
            lastSection = sections.stream()
                .filter(section -> section.isEqualToUpStation(finalDownStation))
                .findFirst()
                .orElseThrow(RuntimeException::new);
            stations.add(lastSection.getDownStation());
        }
        return stations;
    }

    private Section findFirstSection() {
        List<Station> downStations = downStationsOfSections();
        Section firstSection = sections.get(0);
        while (downStations.contains(firstSection.getUpStation())) {
            Station finalUpStation = firstSection.getUpStation();
            firstSection = sections.stream()
                .filter(section -> section.isEqualToDownStation(finalUpStation))
                .findFirst()
                .orElseThrow(RuntimeException::new);
        }
        return firstSection;
    }

    private List<Station> upStationsOfSections() {
        return sections.stream()
            .map(Section::getUpStation)
            .collect(Collectors.toList());
    }

    private List<Station> downStationsOfSections() {
        return sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList());
    }
}
