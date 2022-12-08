package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

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
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public List<Section> values() {
        return Collections.unmodifiableList(sections);
    }

    public void add(Section section) {
        this.sections.add(section);
    }

    public void remove(Section section) {
        this.sections.remove(section);
    }

    public boolean isStationExisted(Station target) {
        return this.getStations().stream().anyMatch(station -> station.equals(target));
    }

    public void updateUpStation(Station upStation, Station downStation, int distance) {
        this.sections.stream()
                .filter(station -> station.getUpStation().equals(upStation))
                .findFirst()
                .ifPresent(station -> station.updateUpStation(downStation, distance));
    }

    public void updateDownStation(Station upStation, Station downStation, int distance) {
        this.sections.stream()
                .filter(station -> station.getDownStation().equals(downStation))
                .findFirst()
                .ifPresent(station -> station.updateDownStation(upStation, distance));
    }

    public Optional<Section> getUpStation(Station station) {
        return this.sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findFirst();
    }

    public Optional<Section> getDownStation(Station station) {
        return this.sections.stream()
                .filter(section -> section.getDownStation().equals(station))
                .findFirst();
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.sections.stream()
                    .filter(section -> section.getUpStation().equals(finalDownStation))
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
        Station downStation = this.sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.sections.stream()
                    .filter(section -> section.getDownStation().equals(finalDownStation))
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }
}
