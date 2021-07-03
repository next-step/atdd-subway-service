package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void add(Section section) {
        this.sections.add(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        return getSortedStation();
    }

    private List<Station> getSortedStation() {
        List<Station> stations = new ArrayList<>();

        Station downStation = findUpStation();
        stations.add(downStation);

        stations.addAll(addNextStation(downStation));

        return stations;
    }

    private List<Station> addNextStation(Station downStation) {
        List<Station> stations = new ArrayList<>();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();

            downStation = addNextDownStationAndReturn(stations, nextLineStation);
        }
        return stations;
    }

    private Station addNextDownStationAndReturn(List<Station> stations, Optional<Section> nextLineStation) {
        if (!nextLineStation.isPresent()) {
            return null;
        }
        Station downStation = nextLineStation.get().getDownStation();
        stations.add(downStation);
        return downStation;
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        boolean hasNext = true;
        while (hasNext) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            hasNext = checkHasNext(nextLineStation);
            downStation = getLastDownStation(hasNext, downStation, nextLineStation);
        }
        return downStation;
    }

    private Station getLastDownStation(boolean hasNext, Station downStation, Optional<Section> nextLineStation) {
        if (hasNext) {
            return nextLineStation.get().getUpStation();
        }
            return downStation;
    }

    private boolean checkHasNext(Optional<Section> nextLineStation) {
        if (!nextLineStation.isPresent()) {
            return false;
        }
        return true;
    }

    public void addStation(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        duplicateCheck(isUpStationExisted(upStation, stations), isDownStationExisted(downStation, stations));
        validateCheck(upStation, downStation, stations);
        addNewSection(line, upStation, downStation, stations, distance);
    }

    private void duplicateCheck(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validateCheck(Station upStation, Station downStation, List<Station> stations) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void addNewSection(Line line, Station upStation, Station downStation, List<Station> stations, int distance) {
        if (stations.isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }
        if (isUpStationExisted(upStation, stations)) {
            updateUpStation(line, upStation, downStation, distance);
            return;
        }
        if (isDownStationExisted(downStation, stations)) {
            updateDownStation(line, upStation, downStation, distance);
            return;
        }
        throw new RuntimeException("등록할 수 없는 구간 입니다.");
    }

    private boolean isUpStationExisted(Station upStation, List<Station> stations) {
        return stations.stream().anyMatch(it -> it == upStation);
    }

    private boolean isDownStationExisted(Station downStation, List<Station> stations) {
        return stations.stream().anyMatch(it -> it == downStation);
    }


    private void updateUpStation(Line line, Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));

        sections.add(new Section(line, upStation, downStation, distance));
    }

    private void updateDownStation(Line line, Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));

        sections.add(new Section(line, upStation, downStation, distance));
    }


    public void removeStation(Line line, Station station) {
        CheckSectionSize();

        Optional<Section> upLineStation = getUpLineStation(station);

        Optional<Section> downLineStation = getDownLineStation(station);

        removeStationInLine(line, upLineStation, downLineStation);
    }

    private Optional<Section> getUpLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private Optional<Section> getDownLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private void removeStationInLine(Line line, Optional<Section> upLineStation, Optional<Section> downLineStation) {
        addNewMiddleSection(line, upLineStation, downLineStation);

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void addNewMiddleSection(Line line, Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }
    }

    private void CheckSectionSize() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }

    public List<Section> values() {
        return sections;
    }
}
