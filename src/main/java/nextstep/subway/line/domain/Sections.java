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
            return;
        }
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public int size() {
        return values.size();
    }

    public void remove(Section section) {
        values.remove(section);
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
}
