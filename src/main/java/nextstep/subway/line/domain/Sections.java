package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private List<Section> sections = new ArrayList<>();

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station firstStation = findFirstStation();
        stations.add(firstStation);
        return Collections.unmodifiableList(addDownStations(firstStation, stations));
    }

    private List<Station> addDownStations(Station firstStation, List<Station> stations) {
        Station downStation = findDownStation(firstStation);
        if (downStation == null) {
            return stations;
        }
        stations.add(downStation);
        return addDownStations(downStation, stations);
    }

    private Station findFirstStation() {
        Station downStation = sections.get(0).getUpStation();
        while (true) {
            Station upStation = findUpStation(downStation);
            if (upStation == null) {
                break;
            }
            downStation = upStation;
        }
        return downStation;
    }

    private Station findUpStation(Station station) {
        Optional<Section> nextLineStation = sections.stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();
        return nextLineStation.map(Section::getUpStation).orElse(null);
    }

    private Station findDownStation(Station station) {
        Optional<Section> nextLineStation = sections.stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
        return nextLineStation.map(Section::getDownStation).orElse(null);
    }


}
