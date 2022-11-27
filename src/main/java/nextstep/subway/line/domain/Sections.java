package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
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
        return getSections().stream()
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
        Optional<Section> upLineStation = getSections().stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = getSections().stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            getSections().add(new Section(line, newUpStation, newDownStation, newDistance));
        }
        upLineStation.ifPresent(it -> getSections().remove(it));
        downLineStation.ifPresent(it -> getSections().remove(it));
    }
}
