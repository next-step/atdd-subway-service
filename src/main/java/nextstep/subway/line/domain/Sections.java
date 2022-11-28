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

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "line",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public void remove(Section section) {
        sections.remove(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Station getFirstUpStation() {
        return sections.get(0).getUpStation();
    }

    public Optional<Section> getDownLineStation(Station finalDownStation) {
        return sections.stream()
                .filter(it -> it.getDownStation() == finalDownStation)
                .findFirst();
    }

    public Optional<Section> getUpLineStation(Station finalUpStation) {
        return sections.stream()
                .filter(it -> it.getUpStation() == finalUpStation)
                .findFirst();
    }

    public void updateUpStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    public void updateDownStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    public boolean isLessThenSize(int size) {
        return sections.size() <= size;
    }

    public int size() {
        return sections.size();
    }
}
