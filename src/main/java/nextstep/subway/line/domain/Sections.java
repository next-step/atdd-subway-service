package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public int size() {
        return sections.size();
    }

    public void addSection(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        int distance = section.getDistance();
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it.equals(upStation));
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it.equals(downStation));

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it.equals(upStation)) &&
                stations.stream().noneMatch(it -> it.equals(downStation))) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted) {
            sections.stream()
                    .filter(it -> it.getUpStation().equals(upStation))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            sections.add(section);
        } else if (isDownStationExisted) {
            sections.stream()
                    .filter(it -> it.getDownStation().equals(downStation))
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            sections.add(section);
        } else {
            throw new RuntimeException();
        }
    }

    public Station findUpStation() {
        return getUpStations().stream()
                .filter(station -> !getDownStations().contains(station))
                .findFirst()
                .orElse(null);
    }

    private List<Station> getUpStations() {
        return sections.stream().map(section -> section.getUpStation())
                .collect(Collectors.toList());
    }

    private List<Station> getDownStations() {
        return sections.stream().map(section -> section.getDownStation())
                .collect(Collectors.toList());
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }
        Station downStation = findUpStation();
        List<Station> stations = new ArrayList<>();
        stations.add(downStation);
        addNextStation(stations, downStation);
        return stations;
    }

    private void addNextStation(List<Station> stations, Station station) {
        while (station != null) {
            Station nextStation = getNextStation(station);
            addStation(stations, nextStation);
            station = nextStation;
        }
    }

    private Station getNextStation(Station finalDownStation) {
        return sections.stream()
                .filter(it -> it.getUpStation().equals(finalDownStation))
                .findFirst()
                .map(section -> section.getDownStation())
                .orElse(null);
    }

    private void addStation(List<Station> stations, Station station) {
        if (station == null) {
            return;
        }
        stations.add(station);
    }

    public void removeStation(Station station, Line line) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
        Section upLineStation = getUpLineStation(station);
        Section downLineStation = getDownLineStation(station);
        removeSection(upLineStation);
        removeSection(downLineStation);
        rebuildSection(line, upLineStation, downLineStation);
    }

    private void removeSection(Section section) {
        if (section == null) {
            return;
        }
        sections.remove(section);
    }

    private void rebuildSection(Line line, Section upLineStation, Section downLineStation) {
        if (upLineStation == null || downLineStation == null) {
            return;
        }
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        addSection(new Section(line, newUpStation, newDownStation, newDistance));

    }

    private Section getUpLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst().orElse(null);
    }

    private Section getDownLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation().equals(station))
                .findFirst().orElse(null);
    }

}
