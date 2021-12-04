package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public Station getEndUpStation() {
        return this.sections.get(0).getUpStation();
    }

    public Optional<Section> getSectionDownStationSame(Station station) {
        return  this.sections.stream()
                .filter(it -> it.getDownStation().equals(station))
                .findFirst();
    }

    public Optional<Section> getSectionUpStationSame(Station station) {
        return  this.sections.stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst();
    }

    public void updateUpStation(Station upStation, Station downStation, int distance) {
        this.sections.stream()
                .filter(it -> it.getUpStation().equals(upStation))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    public void updateDownStation(Station upStation, Station downStation, int distance) {

        this.sections.stream()
                .filter(it -> it.getDownStation().equals(downStation))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));

    }

    @Override
    public String toString() {
        return "Sections{" +
                "sections=" + sections +
                '}';
    }
}
