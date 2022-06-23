package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> values;

    protected Sections() {
        this(new ArrayList<>());
    }

    protected Sections(List<Section> values) {
        this.values = copy(values);
    }

    private static List<Section> copy(List<Section> sections) {
        return sections.stream().map(Section::from).collect(Collectors.toList());
    }

    public List<Section> get() {
        return Collections.unmodifiableList(values);
    }

    public int size() {
        return values.size();
    }

    public void add(Section section) {
        values.add(section);
    }

    public void remove(Section section) {
        values.remove(section);
    }

    public List<Station> getStations() {
        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findFinalUpStation();
        stations.add(downStation);

        return findAllDownStation(stations, downStation);
    }

    private List<Station> findAllDownStation(List<Station> stations, Station finalUpStation) {
        Optional<Section> nextLineStation = findSectionByUpStation(finalUpStation);
        if (!nextLineStation.isPresent()) {
            return stations;
        }
        Station newStation = nextLineStation.get().getDownStation();
        List<Station> newStations = Stream.concat(stations.stream(), Stream.of(newStation))
                .collect(Collectors.toList());
        return findAllDownStation(newStations, newStation);
    }

    private Optional<Section> findSectionByUpStation(Station upStation) {
        return values.stream().filter(it -> it.hasUpStation(upStation)).findFirst();
    }

    public Station findFinalUpStation() {
        if (values.isEmpty()) {
            return null;
        }
        return findUpStation(values.get(0).getUpStation());
    }

    private Station findUpStation(Station downStation) {
        Optional<Section> nextLineStation = findSectionByDownStation(downStation);
        if (!nextLineStation.isPresent()) {
            return downStation;
        }
        return findUpStation(nextLineStation.get().getUpStation());
    }

    private Optional<Section> findSectionByDownStation(Station downStation) {
        return values.stream().filter(it -> it.hasDownStation(downStation)).findFirst();
    }
}
