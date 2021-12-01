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

    public void removeByStation(Station station) {
        validateRemovableSize();
        Optional<Section> upLineSection = findByUpStation(station);
        Optional<Section> downLineSection = findByDownStation(station);
        if (upLineSection.isPresent() && downLineSection.isPresent()) {
            addNewSection(upLineSection.get(), downLineSection.get());
        }
        upLineSection.ifPresent(sections::remove);
        downLineSection.ifPresent(sections::remove);
    }

    private void addNewSection(Section upLineSection, Section downLineSection) {
        Station newUpStation = downLineSection.getUpStation();
        Station newDownStation = upLineSection.getDownStation();
        int newDistance = upLineSection.getDistance() + downLineSection.getDistance();
        sections.add(new Section(upLineSection.getLine(), newUpStation, newDownStation, newDistance));
    }

    private Optional<Section> findByUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualsUpStation(station))
                .findFirst();
    }

    private Optional<Section> findByDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualsDownStation(station))
                .findFirst();
    }

    private void validateRemovableSize() {
        if (sections.size() <= 1) {
            throw new RuntimeException("구간이 하나인 노선은 제거할 수 없습니다.");
        }
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
