package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void addFirstSection(Section section) {
        this.sections.add(section);
    }

    public boolean isEmptySections() {
        return sections.isEmpty();
    }

    public void addSection(Section section) {
        List<Station> stations = getStations();
        validationSection(section, stations);

        boolean isUpStationExisted = isExistStation(stations, section.getUpStation());
        boolean isDownStationExisted = isExistStation(stations, section.getDownStation());

        if (isUpStationExisted) {
            updateUpStationIfSameUpStation(section);
        } else if (isDownStationExisted) {
            updateDownStationIfSameDownStation(section);
        }
        sections.add(section);
    }

    private void validationSection(Section section, List<Station> stations) {
        boolean isUpStationExisted = isExistStation(stations, section.getUpStation());
        boolean isDownStationExisted = isExistStation(stations, section.getDownStation());

        if (isUpStationExisted == isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 또는 등록할수 없는 구간입니다.");
        }
    }

    private boolean isExistStation(List<Station> stations, Station station) {
        return stations.stream().anyMatch(it -> it == station);
    }

    private void updateUpStationIfSameUpStation(Section section) {
        sections.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void updateDownStationIfSameDownStation(Section section) {
        sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    private Optional<Section> getSection(Predicate<Section> sectionPredicate) {
        return sections.stream()
                .filter(sectionPredicate)
                .findFirst();
    }

    public List<Station> getStations() {

        if (isEmptySections()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextSection = getSection(section -> section.isSameUpStation(finalDownStation));
            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;

            Optional<Section> nextSection = getSection(section -> section.isSameDownStation(finalDownStation));
            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getUpStation();
        }

        return downStation;
    }

    private Station getUpStation() {
        List<Station> downStations = sections.stream()
                .map(section -> section.getDownStation())
                .collect(Collectors.toList());

        Section firstSection = sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("상행역이 존재하지않습니다."));

        return firstSection.getUpStation();
    }

    public Section removeLineStation(Station station) {
        if (isUnableRemoveStatus()) {
            throw new RuntimeException("해당노선에 등록된 구간이 1개 이하일 경우 역을 삭제할수 없습니다.");
        }

        Optional<Section> upLineStation = getSection(section -> section.isSameUpStation(station));
        Optional<Section> downLineStation = getSection(section -> section.isSameDownStation(station));

        upLineStation.ifPresent(it -> removeSection(it));
        downLineStation.ifPresent(it -> removeSection(it));

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            Section section = new Section(newUpStation, newDownStation, newDistance);
            sections.add(section);

            return section;
        }

        return null;
    }

    private void removeSection(Section section) {
        sections.remove(section);
    }

    private boolean isUnableRemoveStatus() {
        return sections.size() <= 1;
    }
}
