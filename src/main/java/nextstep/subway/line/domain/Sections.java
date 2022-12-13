package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public int size() {
        return sections.size();
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Stream<Section> stream() {
        return sections.stream();
    }

    public void add(Section section) {
        if (section != null) {
            sections.add(section);
        }
    }

    public void remove(Section section) {
        if (section != null) {
            sections.remove(section);
        }
    }

    public Station getFirstSectionDownStation() {
        return sections.get(0).getDownStation();
    }

    public List<Section> getSections() {
        return sections;
    }
}
