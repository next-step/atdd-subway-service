package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {

    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections of(Section... sections) {
        return new Sections(new ArrayList<>(Arrays.asList(sections)));
    }

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {
        sections.add(section);
    }

    public List<Station> getOrderedStations() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }

        Station firstUpStation = findFirstUpStation();
        return makeOrderedStations(firstUpStation);
    }

    private List<Station> makeOrderedStations(Station downStation) {
        List<Station> stations = new ArrayList<>();
        stations.add(downStation);

        while (downStation != null) {
            Optional<Section> nextSection = findNextDownSection(downStation);

            if (!nextSection.isPresent()) {
                break;
            }

            downStation = nextSection.get().getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    private Station findFirstUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Optional<Section> nextSection = findNextUpSection(downStation);

            if (!nextSection.isPresent()) {
                break;
            }

            downStation = nextSection.get().getUpStation();
        }

        return downStation;
    }

    private Optional<Section> findNextDownSection(Station downStation) {
        return sections.stream()
                .filter(it -> it.hasSameUpStation(downStation))
                .findFirst();
    }

    private Optional<Section> findNextUpSection(Station downStation) {
        return sections.stream()
                .filter(it -> it.hasSameDownStation(downStation))
                .findFirst();
    }
}
