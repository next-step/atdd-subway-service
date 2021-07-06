package nextstep.subway.line.domain;

import nextstep.subway.exception.NotValidateRemovalSectionsSizeException;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    public static final int MINIMUM_REMOVAL_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    void removeByStation(Station station, Line line) {
        Optional<Section> upLineStation = findSectionByUpStation(station);
        Optional<Section> downLineStation = findSectionByDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section downSection = downLineStation.get();
            Section upSection = upLineStation.get();

            Station newUpStation = downSection.getUpStation();
            Station newDownStation = upSection.getDownStation();

            int newDistance = upSection.addDistance(downSection);
            add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(this::remove);
        downLineStation.ifPresent(this::remove);
    }

    Optional<Section> findSectionByUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    public void add(Section section) {
        sections.add(section);
    }

    void remove(Section section) {
        sections.remove(section);
    }

    void updateUpToDownStationWhenExist(SectionRequest request, Station upStation, Station downStation) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, request.getDistance()));
    }

    void updateDownToUpStationWhenExist(SectionRequest request, Station upStation, Station downStation) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, request.getDistance()));
    }

    Optional<Section> findNextSectionByUpStation(Station finalDownStation) {
        return sections.stream()
                .filter(it -> it.getUpStation().equals(finalDownStation))
                .findFirst();
    }

    Optional<Section> findNextSectionByDownStation(Station finalDownStation) {
        return sections.stream()
                .filter(it -> it.getDownStation().equals(finalDownStation))
                .findFirst();
    }

    Station getFirstUpStation() {
        return sections.get(0).getUpStation();
    }

    void validateRemovalSectionsSize() {
        if (sections.size() <= MINIMUM_REMOVAL_SIZE) {
            throw new NotValidateRemovalSectionsSizeException();
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }
}
