package nextstep.subway.line.domain.collections;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Station findDepartStation() {
        Section notOrderFirstSection = sections.stream().findFirst().orElseThrow(RuntimeException::new);
        Station departStation = notOrderFirstSection.getUpStation();

        while (isExistUpSection(departStation)) {
            final Station finalDepartStation = departStation;
            Section nextUpSection = sections.stream()
                    .filter(section -> section.getDownStation().equals(finalDepartStation))
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
        Station departStation = findDepartStation();
        return sortStationsBySection(departStation);
    }

    private List<Station> sortStationsBySection(Station upStation) {
        List<Station> stations = new ArrayList<>();
        stations.add(upStation);

        while (isExistDownStation(upStation)) {
            final Station finalUpStation = upStation;
            Section nextDownSection = sections.stream()
                    .filter(section -> section.getUpStation().equals(finalUpStation))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
            upStation = nextDownSection.getDownStation();
            stations.add(upStation);
        }

        return stations;
    }

    private boolean isExistDownStation(Station upSection) {
        return sections.stream().anyMatch(section -> section.getUpStation().equals(upSection));
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void createNewSection(Line line, Station upStation, Station downStation, long distance) {

    }
}
