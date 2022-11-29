package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidRequestException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "line",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public void remove(Section section) {
        sections.remove(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();

        stations.add(findUpStation());
        stations.addAll(getSortedDownStations());

        return stations;
    }

    public void addStation(Station upStation, Station downStation, int distance, Line line) {
        List<Station> stations = getStations();

        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        checkExistAlreadySection(isUpStationExisted, isDownStationExisted);
        checkValidAddedSection(stations, upStation, downStation);

        Section section = new Section(line, upStation, downStation, distance);
        sections.add(updateSection(section, isUpStationExisted, isDownStationExisted));
    }

    public void removeStation(Station station, Line line) {
        Optional<Section> upLineStation = getUpLineStation(station);
        Optional<Section> downLineStation = getDownLineStation(station);

        addedResetRemoveSection(upLineStation, downLineStation, line);

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    public Optional<Section> getDownLineStation(Station finalDownStation) {
        return sections.stream()
                .filter(it -> it.getDownStation() == finalDownStation)
                .findFirst();
    }

    public Optional<Section> getUpLineStation(Station finalUpStation) {
        return sections.stream()
                .filter(it -> it.getUpStation() == finalUpStation)
                .findFirst();
    }

    public void updateUpStation(Section section) {
        sections.stream()
                .filter(it -> it.equalsUpStation(section))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section));
    }

    public void updateDownStation(Section section) {
        sections.stream()
                .filter(it -> it.equalsDownStation(section))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section));
    }

    public boolean isLessThenSize(int size) {
        return sections.size() <= size;
    }

    public int size() {
        return sections.size();
    }

    private Station findUpStation() {
        List<Station> downStations = getSortedDownStations();
        return sections.stream()
                .map(Section::getUpStation)
                .filter(station -> !downStations.contains(station))
                .findFirst()
                .orElse(null);
    }

    private List<Station> getSortedDownStations() {
        return sections.stream()
                .sorted()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    private void checkValidAddedSection(List<Station> stations, Station upStation, Station downStation) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new InvalidRequestException("등록할 수 없는 구간 입니다.");
        }
    }

    private void checkExistAlreadySection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new InvalidRequestException("이미 등록된 구간 입니다.");
        }
    }

    private Section updateSection(Section section, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isEmpty()) {
            return section;
        }

        if (isUpStationExisted) {
            updateUpStation(section);
            return section;
        }

        if (isDownStationExisted) {
            updateDownStation(section);
            return section;
        }
        throw new InvalidRequestException("추가할수 있는 지하철 역의 구간이 없습니다.");
    }

    private void addedResetRemoveSection(Optional<Section> upLineStation, Optional<Section> downLineStation, Line line) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            int changeDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(
                    line,
                    downLineStation.get().getUpStation(),
                    upLineStation.get().getDownStation(),
                    changeDistance));
        }
    }

}
