package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections;

    public Sections() {
        sections = new ArrayList<>();
    }

    public void add(Section section) {
        if (!sections.isEmpty()) {
            validateAddSections(section);
            reArrangeAddSections(section);
        }
        sections.add(section);
    }

    private void reArrangeAddSections(Section addSection) {
        sections.stream()
                .filter(section -> section.isIncludeSection(addSection))
                .findFirst()
                .ifPresent(section -> section.updateStationByAddSection(addSection));
    }

    private void validateAddSections(Section section) {
        Stations stations = this.getStations();
        boolean isUpStationExisted = stations.isIncluded(section.getUpStation());
        boolean isDownStationExisted = stations.isIncluded(section.getDownStation());
        alreadyAddSection(isUpStationExisted, isDownStationExisted);
        notIncludeOneStation(isUpStationExisted, isDownStationExisted);
    }

    private void notIncludeOneStation(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("두 지하철역 중 하나는 등록 되어 있어야 합니다.");
        }
    }

    private void alreadyAddSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    public Stations getStations() {
        if (sections.isEmpty()) {
            return new Stations();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return new Stations(stations);
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sections)) return false;
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
