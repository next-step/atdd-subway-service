package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public Optional<Section> findSection(Predicate<Section> sectionPredicate) {
        return this.sections.stream()
                .filter(sectionPredicate)
                .findFirst();
    }

    public Station findAnyDownStation() {
        return this.sections.get(0).getDownStation();
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return new ArrayList<>();
        }

        List<Station> stations = new ArrayList<>();
        Station upStation = findUpStation();
        stations.add(upStation);
        addStations(stations, upStation);

        return stations;
    }

    private void addStations(List<Station> stations, Station downStation) {
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextSection = findSection(it -> it.isEqualsUpStation(finalDownStation));

            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getDownStation();
            stations.add(downStation);
        }
    }

    private Station findUpStation() {
        Station downStation = findAnyDownStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findSection(it -> it.isEqualsDownStation(finalDownStation));

            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }

    public boolean isLessThanOrEqual(int size) {
        return this.sections.size() <= size;
    }

    public List<Section> getSections() {
        return sections;
    }
}
