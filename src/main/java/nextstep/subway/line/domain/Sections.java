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
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    public Sections() {
        sections = new ArrayList<>();
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        if(sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station station = findUpStation();
        stations.add(station);

        Optional<Section> nextLineStation = findStationAsUpStation(station);
        while(nextLineStation.isPresent()) {
            station = nextLineStation.get().getDownStation();
            stations.add(station);

            nextLineStation = findStationAsUpStation(station);
        }

        return stations;
    }

    private Station findUpStation() {
        Station station = sections.get(0).getUpStation();

        Optional<Section> nextLineStation = findStationAsDownStation(station);
        while(nextLineStation.isPresent()) {
            station = nextLineStation.get().getUpStation();
            nextLineStation = findStationAsDownStation(station);
        }

        return station;
    }

    private Optional<Section> findStationAsUpStation(Station station) {
        return sections.stream()
            .filter(it -> it.isEqualToUpStation(station))
            .findFirst();
    }

    private Optional<Section> findStationAsDownStation(Station station) {
        return sections.stream()
            .filter(it -> it.isEqualToDownStation(station))
            .findFirst();
    }
}
