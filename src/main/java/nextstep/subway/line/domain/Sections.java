package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> values = new ArrayList<>();

    public List<Section> getValues() {
        return Collections.unmodifiableList(new ArrayList<>(values));
    }

    public void add(Section section) {
        Stations stations = new Stations(section.getLine().getStations());

        stations.validateDuplication(section.getUpStation(), section.getDownStation());
        stations.validateStation(section.getUpStation(), section.getDownStation());

        if (stations.isEmpty()) {
            values.add(section);
            return;
        }

        if (stations.isExisted(section.getUpStation())) {
            updateUpStation(section.getUpStation(), section.getDownStation(), section.getDistance());
            values.add(section);
            return;
        }
        if (stations.isExisted(section.getDownStation())) {
            updateDownStation(section.getUpStation(), section.getDownStation(), section.getDistance());
            values.add(section);
        }
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public int size() {
        return values.size();
    }

    public Station findDownStation() {
        return findFirstSection().getUpStation();
    }

    private Section findFirstSection() {
        return values.get(0);
    }

    public Optional<Section> findNextLineUpStation(Station finalDownStation) {
        return values.stream()
                .filter(it -> it.getUpStation() == finalDownStation)
                .findFirst();
    }

    public Optional<Section> findNextLineDownStation(Station finalDownStation) {
        return values.stream()
                .filter(it -> it.getDownStation() == finalDownStation)
                .findFirst();
    }

    public void updateUpStation(Station upStation, Station downStation, int distance) {
        values.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    public void updateDownStation(Station upStation, Station downStation, int distance) {
        values.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    public void cutOff(Line line, Station station) {
        validateSize();
        readjustDistance(line, station);
        cutOffUpLineStation(station);
        cutOffDownLineStation(station);
    }

    private void readjustDistance(Line line, Station station) {
        if (isBidirectionalPresent(station)) {
            Station newUpStation = getNewUpStation(station);
            Station newDownStation = getNewDownStation(station);
            int newDistance = findUpDistance(station) + findDownDistance(station);
            values.add(new Section(line, newUpStation, newDownStation, newDistance));
        }
    }

    private void cutOffUpLineStation(Station station) {
        values.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst()
                .ifPresent(v -> values.remove(v));
    }

    private void cutOffDownLineStation(Station station) {
        values.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst()
                .ifPresent(v -> values.remove(v));
    }

    private int findUpDistance(Station station) {
        return values.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst()
                .map(Section::getDistance)
                .orElseThrow(RuntimeException::new);
    }

    private int findDownDistance(Station station) {
        return values.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst()
                .map(Section::getDistance)
                .orElseThrow(RuntimeException::new);
    }

    private Station getNewUpStation(Station station) {
        return values.stream()
                .filter(v -> v.getDownStation() == station)
                .findFirst()
                .map(Section::getUpStation)
                .orElseThrow(RuntimeException::new);
    }

    private Station getNewDownStation(Station station) {
        return values.stream()
                .filter(v -> v.getUpStation() == station)
                .findFirst()
                .map(Section::getDownStation)
                .orElseThrow(RuntimeException::new);
    }

    private boolean isBidirectionalPresent(Station station) {
        return isUpStationPresent(station) && isDownStationPresent(station);
    }

    private boolean isUpStationPresent(Station station) {
        return values.stream()
                .anyMatch(it -> it.getUpStation() == station);
    }

    private boolean isDownStationPresent(Station station) {
        return values.stream()
                .anyMatch(it -> it.getDownStation() == station);
    }

    private void validateSize() {
        if (values.size() <= 1) {
            throw new RuntimeException();
        }
    }
}
