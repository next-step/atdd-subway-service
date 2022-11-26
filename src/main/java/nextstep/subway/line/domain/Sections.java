package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        if (!sections.isEmpty()) {
            validateStations(section);
            ifConnectedUpStation(section);
            ifConnectedDownStation(section);
        }
        sections.add(section);
    }

    private void validateStations(Section section) {
        boolean isExistsUpStation = containUpStation(section);
        boolean isExistsDownStation = containDownStation(section);
        if (isExistsUpStation && isExistsDownStation) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");

        }
        if (!isExistsUpStation && !isExistsDownStation) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");

        }
    }

    private boolean containUpStation(Section section) {
        return distinctStations().contains(section.getUpStation());
    }

    private boolean containDownStation(Section section) {
        return distinctStations().contains(section.getDownStation());
    }

    private List<Station> distinctStations() {
        return sections.stream()
                .flatMap(Section::streamOfStation)
                .distinct()
                .collect(Collectors.toList());
    }

    private void ifConnectedUpStation(Section addSection) {
        sections.forEach(section -> checkConnectedUpStation(addSection, section));
    }

    private void checkConnectedUpStation(Section addSection, Section section) {
        if (section.getUpStation().equals(addSection.getUpStation())) {
            section.connectUpStationToDownStation(addSection);
        }
    }

    private void ifConnectedDownStation(Section addSection) {
        sections.forEach(section -> checkConnectedDownStation(addSection, section));
    }

    private void checkConnectedDownStation(Section addSection, Section section) {
        if (section.getDownStation().equals(addSection.getDownStation())) {
            section.connectDownStationToUpStation(addSection);
        }
    }


}
