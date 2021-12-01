package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Sections {
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_line_section"))
    private List<Section> sections = new ArrayList<>();

    protected Sections() {

    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections of(List<Section> sections) {
        return new Sections(sections);
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Section downStation = findUpStation();
        stations.add(downStation.getUpStation());

        while (downStation != null) {
            stations.add(downStation.getDownStation());
            Station nextUpStation = downStation.getDownStation();

            downStation = sections.stream()
                    .filter(section -> section.isEqualToUpStation(nextUpStation))
                    .findFirst()
                    .orElse(null);
        }

        return stations;
    }

    private Section findUpStation() {
        Section downSection = sections.get(0);
        assert (downSection != null);
        Section resultUpSection = null;

        while (downSection != null) {
            resultUpSection = downSection;
            Station nextDownStation = downSection.getUpStation();

            downSection = sections.stream()
                    .filter(section -> section.isEqualToDownStation(nextDownStation))
                    .findFirst()
                    .orElse(null);
        }

        return resultUpSection;
    }
}
