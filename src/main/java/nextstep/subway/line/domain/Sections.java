package nextstep.subway.line.domain;

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

    protected Sections() {
    }

    protected Sections(Section section) {
        this.sections.add(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findFirstStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findPostSection(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findFirstStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findPreviousSection(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void addSection(Section sectionToAdd) {
        validateSectionToAdd(sectionToAdd);
        updateAdjacentSection(sectionToAdd);
        sections.add(sectionToAdd);
    }

    private void updateAdjacentSection(Section sectionToAdd) {
        sections.stream().forEach(section -> section.updateSection(sectionToAdd));
    }

    private void validateSectionToAdd(Section sectionToAdd) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.contains(sectionToAdd.getUpStation());
        boolean isDownStationExisted = stations.contains(sectionToAdd.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public void removeStation(Station station) {
        validateStationToRemove();
        removeValidStation(station);
    }

    private void removeValidStation(Station station) {
        Optional<Section> postSection = findPostSection(station);
        Optional<Section> previousSection = findPreviousSection(station);
        if (postSection.isPresent() && previousSection.isPresent()) {
            removeMiddleStation(postSection.get(), previousSection.get());
            return;
        }
        removeEndStation(postSection, previousSection);
    }

    private void removeMiddleStation(Section postSection, Section previousSection) {
        previousSection.mergeWith(postSection);
        sections.remove(postSection);
    }

    private void removeEndStation(Optional<Section> postSection, Optional<Section> previousSection) {
        postSection.ifPresent(it -> sections.remove(it));
        previousSection.ifPresent(it -> sections.remove(it));
    }

    private Optional<Section> findPreviousSection(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Optional<Section> findPostSection(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private void validateStationToRemove() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }
}
