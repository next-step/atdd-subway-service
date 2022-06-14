package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}
