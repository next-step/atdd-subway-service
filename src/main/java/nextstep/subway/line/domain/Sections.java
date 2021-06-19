package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> values;

    public Sections(List<Section> values) {
        this.values = values;
    }

    public Sections() {

    }

    public List<Section> getValues() {
        return Collections.unmodifiableList(values);
    }

    public void add(Section section) {
        this.values.add(section);
    }

    public List<Station> getStations() {
        if (this.isEmpty()) {
            return new ArrayList<>();
        }

        List<Station> stations = new ArrayList<>();
        Station upStation = findUpStation();
        stations.add(upStation);

        while (upStation != null) {
            Station finalUpStation = upStation;
            Optional<Section> nextLineStation = this.stream()
                    .filter(it -> it.getUpStation() == finalUpStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            upStation = nextLineStation.get().getDownStation();
            stations.add(upStation);
        }
        return stations;
    }

    public Station findUpStation() {
        Station upStation = this.get(0).getUpStation();

        while (upStation != null) {
            Station finalUpStation = upStation;
            Optional<Section> nextLineStation = this.stream()
                    .filter(it -> it.getDownStation() == finalUpStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            upStation = nextLineStation.get().getUpStation();
        }

        return upStation;
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public Stream<Section> stream() {
        return this.values.stream();
    }

    public Section get(int index) {
        return this.values.get(index);
    }

    public void exist() {
        if (this.values.size() <= 1) {
            throw new RuntimeException();
        }
    }

    public void remove(Section it) {
        this.values.remove(it);
    }

    public Optional<Section> getStationInUpStations(Station station) {
        return this.values.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    public Optional<Section> getStationInDownStations(Station station) {
        return this.values.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    public void changeUpStationIfFindEqualsUpStation(Section section) {
        Station downStation = section.getDownStation();
        Station upStation = section.getUpStation();
        this.values.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, section.getDistance()));
        this.values.add(section);
    }

    public void changeDownStationIfFindEqualsDownStation(Section section) {
        Station downStation = section.getDownStation();
        Station upStation = section.getUpStation();
        this.values.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, section.getDistance()));
        this.values.add(section);
    }
}
