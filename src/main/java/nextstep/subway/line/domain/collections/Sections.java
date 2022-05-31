package nextstep.subway.line.domain.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Station findDepartStation() {
        Section section = sections.stream().findFirst().orElseThrow(RuntimeException::new);
        Station departStation = section.getUpStation();

        while (isExistUpSection(departStation)) {
            final Station finalDepartStation = departStation;
            Section nextUpSection = sections.stream()
                    .filter(it -> it.getDownStation() == finalDepartStation)
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
            departStation = nextUpSection.getUpStation();
        }

        return departStation;
    }

    private boolean isExistUpSection(Station departStation) {
        return sections.stream().anyMatch(section -> section.getDownStation().equals(departStation));
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Station downStation = findDepartStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
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

    public void addSection(Section section) {
        sections.add(section);
    }
}
