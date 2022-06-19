package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public Sections(){
    }

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        return createStations();
    }

    private List<Station> createStations() {
        List<Station> stations = new ArrayList<>();

        Section section = getFirstSection();
        stations.add(section.getUpStation());
        stations.add(section.getDownStation());

        findStations(stations, section);
        return stations;
    }

    private Section getFirstSection() {
        Station station = findUpStation();

        return sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("노선의 역을 찾을 수 없습니다."));
    }

    private Station findUpStation() {
        List<Station> upStations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        return upStations.stream()
                .filter(station -> isNotContainsDownStation(station))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("노선의 역을 찾을 수 없습니다."));
    }

    private boolean isNotContainsDownStation(Station station) {
        return sections.stream()
                .noneMatch(section -> section.getDownStation().equals(station));
    }

    private void findStations(List<Station> stations, Section section) {
        Optional<Section> optionalSection = findNextDownSection(section);

        while (optionalSection.isPresent()) {
            stations.add(optionalSection.get().getDownStation());
            optionalSection = findNextDownSection(optionalSection.get());
        }
    }

    private Optional<Section> findNextDownSection(Section preSection) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(preSection.getDownStation()))
                .findFirst();
    }
}
