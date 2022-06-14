package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> values = new ArrayList<>();

    public List<Section> getValues() {
        return Collections.unmodifiableList(new ArrayList<>(values));
    }

    public void add(Section section) {
        values.add(section);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public int size() {
        return values.size();
    }

    public void remove(Section section) {
        values.remove(section);
    }

    public Station findDownStation() {
        return findFirstSection().getUpStation();
    }

    private Section findFirstSection() {
        return values.get(0);
    }

    public Optional<Section> findNextLineUpStation(Station finalDownStation) {
        return values.stream()
                .filter(it -> it.getUpStation() == finalDownStation)
                .findFirst();
    }

    public Optional<Section> findNextLineDownStation(Station finalDownStation) {
        return values.stream()
                .filter(it -> it.getDownStation() == finalDownStation)
                .findFirst();
    }
}
