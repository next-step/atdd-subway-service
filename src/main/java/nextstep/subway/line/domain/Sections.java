package nextstep.subway.line.domain;

import java.util.ArrayList;
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
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public static Sections of() {
        return new Sections();
    }

    public void add(Section section) {
        sections.add(section);
    }

    public Integer count() {
        return sections.size();
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStationsInOrder() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        return mapStations();
    }

    private List<Station> mapStations() {
        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Optional<Section> nextLineStation = findNextStation(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station firstStation = getFirstStation();
        while (firstStation != null) {
            Station finalDownStation = firstStation;
            Optional<Section> nextLineStation = sections.stream()
                .filter(it -> it.getDownStation() == finalDownStation)
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            firstStation = nextLineStation.get().getUpStation();
        }

        return firstStation;
    }

    private Optional<Section> findNextStation(Station finalDownStation) {
        return sections.stream()
            .filter(it -> it.isNextStation(finalDownStation))
            .findFirst();
    }

    private Station getFirstStation() {
        return sections.get(0).getUpStation();
    }
}
