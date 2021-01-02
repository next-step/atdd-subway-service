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

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {
        sections.add(section);
    }

    public void remove(Section section) {
        sections.remove(section);
    }

    public List<Station> getStations() {
        if (getSections().isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        addUpLineStation(stations, downStation);

        return stations;
    }

    public void addStation(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == section.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == section.getDownStation());

        if (isNotAddPossibleStation(section, stations, isUpStationExisted, isDownStationExisted)) return;

        addPossibleStation(section, isUpStationExisted, isDownStationExisted);
    }

    public void removeStation(Line line, Station station) {
        validateExistStation(station);
        removeStationExecution(line, station);
    }

    private Station findUpStation() {
        Station downStation = getSections().get(0).getUpStation();
        return findDownStation(downStation);
    }

    private Station findDownStation(Station downStation) {
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findDownLineStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }

    private void addUpLineStation(List<Station> stations, Station downStation) {
        stations.add(downStation);
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findUpLineStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
    }

    private Optional<Section> findDownLineStation(Station station) {
        return getSections().stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Optional<Section> findUpLineStation(Station station) {
        return getSections().stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private boolean isNotAddPossibleStation(Section section, List<Station> stations, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
                stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            getSections().add(section);
            return true;
        }
        return false;
    }

    private void addPossibleStation(Section section, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted) {
            addUpStationExisted(section);
            return;
        }
        if (isDownStationExisted) {
            addDownStationExisted(section);
        }
    }

    private void addDownStationExisted(Section section) {
        getSections().stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

        getSections().add(section);
    }

    private void addUpStationExisted(Section section) {
        getSections().stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

        getSections().add(section);
    }

    private void validateExistStation(Station station) {
        if (getSections().size() <= 1) {
            throw new IllegalArgumentException("구간에서 역을 제거할 수 없습니다.");
        }
        if (getStations().stream().noneMatch(it -> it == station)) {
            throw new IllegalArgumentException("제거 할 역이 노선에 없습니다.");
        }
    }

    private void removeStationExecution(Line line, Station station) {
        Optional<Section> upLineStation = findUpLineStation(station);
        Optional<Section> downLineStation = findDownLineStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addNewSection(line, upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(this::remove);
        downLineStation.ifPresent(this::remove);
    }

    private void addNewSection(Line line, Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        add(new Section(line, newUpStation, newDownStation, newDistance));
    }
}
