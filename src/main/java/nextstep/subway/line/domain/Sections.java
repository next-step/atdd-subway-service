package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    public static final int SECTION_LIST_LOWER_BOUND_SIZE = 1;
    @OneToMany(mappedBy = "line", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>();

    public Sections() {
    }

    public void add(Section section) {
        this.sectionList.add(section);
    }

    public List<Station> getStations() {
        if (this.sectionList.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findFirstByUpStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation =
                this.sectionList.stream().findFirst().orElseThrow(NoSuchElementException::new).getUpStation();

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findFirstByDownStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void removeLineStation(Line line, Station station) {
        checkLeft();

        Optional<Section> upLineStation = findFirstByUpStation(station);
        Optional<Section> downLineStation = findFirstByDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> this.sectionList.remove(it));
        downLineStation.ifPresent(it -> this.sectionList.remove(it));
    }

    private void checkLeft() {
        if (this.sectionList.size() <= SECTION_LIST_LOWER_BOUND_SIZE) {
            throw new CannotRemoveException();
        }
    }

    private Optional<Section> findFirstByDownStation(Station station) {
        return this.sectionList.stream().filter(section -> section.isSameDownStation(station)).findFirst();
    }

    private Optional<Section> findFirstByUpStation(Station station) {
        return this.sectionList.stream().filter(section -> section.isSameUpStation(station)).findFirst();
    }

    public void addLineStation(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        checkAlreadyAdded(isUpStationExisted, isDownStationExisted);
        checkEitherOneOfAddedStation(upStation, downStation, stations);

        if (stations.isEmpty()) {
            add(new Section(line, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            findFirstByUpStation(upStation).ifPresent(it -> it.updateUpStation(downStation, distance));
            add(new Section(line, upStation, downStation, distance));
            return;
        }

        findFirstByDownStation(downStation).ifPresent(it -> it.updateDownStation(upStation, distance));
        add(new Section(line, upStation, downStation, distance));
    }

    private void checkEitherOneOfAddedStation(Station upStation, Station downStation, List<Station> stations) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) && stations.stream()
                .noneMatch(it -> it == downStation)) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private void checkAlreadyAdded(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }
}
