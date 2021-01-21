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
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public boolean isNextLineStation(Line line, Station station) {
        return line.getSections().stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst()
                .isPresent();
    }

    public Optional<Section> nextLineStation(Line line, Station station) {
        return line.getSections().stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst();
    }

    public boolean isNextUpstation(Line line, Station station) {
        return line.getSections().stream()
                .filter(it -> it.getDownStation().equals(station))
                .findFirst()
                .isPresent();
    }

    public Optional<Section> nextUpstation(Line line, Station station) {
        return line.getSections().stream()
                .filter(it -> it.getDownStation().equals(station))
                .findFirst();
    }

}