package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class LineSections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> lineSections = new ArrayList<>();

    protected LineSections() {
    }

    public LineSections(List<Section> lineSections) {
        this.lineSections = lineSections;
    }

    public List<Station> toStations() {
        if (lineSections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = lineSections.stream()
                .filter(it -> it.isUpStationEquals(finalDownStation))
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = lineSections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = lineSections.stream()
                .filter(it -> it.isDownStationEquals(finalDownStation))
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void add(Section section) {
        List<Station> stations = toStations();
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        boolean isUpStationExisted = isStationExisted(stations, upStation);
        boolean isDownStationExisted = isStationExisted(stations, downStation);

        validateDuplicate(isUpStationExisted, isDownStationExisted);
        validateNotExist(stations, upStation, downStation);

        if (stations.isEmpty()) {
            lineSections.add(section);
            return;
        }

        if (isUpStationExisted) {
            changeUpStation(section);
        }

        if (isDownStationExisted) {
            changeDownStation(section);
        }
        lineSections.add(section);
    }

    private void changeUpStation(Section section) {
        lineSections.stream()
            .filter(it -> it.isUpStationEquals(section.getUpStation()))
            .findFirst()
            .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void changeDownStation(Section section) {
        lineSections.stream()
            .filter(it -> it.isDownStationEquals(section.getDownStation()))
            .findFirst()
            .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private boolean isStationExisted(List<Station> stations, Station station) {
        return stations.stream().anyMatch(it -> it.equals(station));
    }

    private void validateDuplicate(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validateNotExist(List<Station> stations, Station upStation, Station downStation) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it.equals(upStation)) &&
            stations.stream().noneMatch(it -> it.equals(downStation))) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public void removeSection(Line line, Station station) {
        validateLineSectionsHasOnly();

        Section upLineStation = findUpLineStation(station);
        Section downLineStation = findDownLineStation(station);

        if (upLineStation != null) {
            lineSections.remove(upLineStation);
        }
        if (downLineStation != null) {
            lineSections.remove(downLineStation);
        }

        if (upLineStation != null && downLineStation != null) {
            addNewSection(line, upLineStation, downLineStation);
        }
    }

    private void validateLineSectionsHasOnly() {
        if (lineSections.size() <= 1) {
            throw new RuntimeException();
        }
    }

    private Section findUpLineStation(Station station) {
        return lineSections.stream()
            .filter(it -> it.isUpStationEquals(station))
            .findFirst()
            .orElse(null);
    }

    private Section findDownLineStation(Station station) {
        return lineSections.stream()
            .filter(it -> it.isDownStationEquals(station))
            .findFirst()
            .orElse(null);
    }

    private void addNewSection(Line line, Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        lineSections.add(new Section(line, newUpStation, newDownStation, newDistance));
    }

    public List<Section> getLineSections() {
        return lineSections;
    }
}
