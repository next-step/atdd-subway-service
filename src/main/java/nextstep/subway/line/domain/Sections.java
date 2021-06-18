package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.function.Predicate;

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

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
                stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
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
            Optional<Section> nextLineStation = getSection(section -> section.isSameUpStation(finalDownStation));
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Collections.sort(sections);

        Station downStation = getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;

            Optional<Section> nextLineStation = getSection(section -> section.isSameDownStation(finalDownStation));
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private Station getUpStation() {
        return sections.get(0).getUpStation();
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
