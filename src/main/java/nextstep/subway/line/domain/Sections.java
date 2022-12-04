package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.line.excpetion.LastSectionException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }
        List<Station> stations = new ArrayList<>();
        stations.add(findFirstStation());
        stations.addAll(findStationsFrom(stations.get(0)));
        return stations;
    }

    public void add(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == section.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == section.getDownStation());
        validateAllAlreadyExisted(isUpStationExisted, isDownStationExisted);
        validateSections(stations, section);
        addWhenUpStationExisted(section);
        addWhenDownStationExisted(section);
        this.sections.add(section);
    }

    public void remove(Station station) {
        validateLastSection();

        Optional<Section> upLineStation = findByUpStation(station);
        Optional<Section> downLineStation = findByDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addWhenMiddleStationRemove(upLineStation.get(), downLineStation.get());
        }
        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));
    }

    private void addWhenMiddleStationRemove(Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        this.sections.add(new Section(upLineStation.getLine(), newUpStation, newDownStation, newDistance));
    }

    private void validateLastSection() {
        if (this.sections.size() <= 1) {
            throw new LastSectionException();
        }
    }

    private void addWhenDownStationExisted(Section section) {
        findByDownStation(section.getDownStation())
            .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void addWhenUpStationExisted(Section section) {
        findByUpStation(section.getUpStation())
            .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void validateSections(List<Station> stations, Section section) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
            stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateAllAlreadyExisted(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private List<Station> findStationsFrom(Station upStation) {
        List<Station> stations = new ArrayList<>();
        Station downStation = upStation;
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findByUpStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    private Station findFirstStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findByDownStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private Optional<Section> findByUpStation(Station upStation) {
        return this.sections.stream()
            .filter(it -> it.getUpStation() == upStation)
            .findFirst();
    }

    private Optional<Section> findByDownStation(Station downStation) {
        return this.sections.stream()
            .filter(it -> it.getDownStation() == downStation)
            .findFirst();
    }
}
