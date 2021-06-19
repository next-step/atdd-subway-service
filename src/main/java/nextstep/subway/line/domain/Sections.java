package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void add(Section section) {
        this.values.add(section);
    }

    public List<Station> getStations() {
        if (this.isEmpty()) {
            return new ArrayList<>();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    public Station findUpStation() {
        Station downStation = this.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
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
}
