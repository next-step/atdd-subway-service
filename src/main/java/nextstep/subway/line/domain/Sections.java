package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    public Sections() {
        sections = new ArrayList<>();
    }

    private Station findFirstStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findSectionWithDownStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Station downStation = findFirstStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findSectionWithUpStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public Optional<Section> findSectionWithUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    public Optional<Section> findSectionWithDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public boolean isNotRemovable() {
        return sections.size() <= 1;
    }
}
