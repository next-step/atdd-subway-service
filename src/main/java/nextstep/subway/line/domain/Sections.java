package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.stream.Stream;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> values;

    public Sections(List<Section> values) {
        this.values = values;
    }

    public Sections() {

    }

    public void add(Section section) {
        this.values.add(section);
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public Stream<Section> stream() {
        return this.values.stream();
    }

    public Section get(int index) {
        return this.values.get(index);
    }

    public void exist() {
        if (this.values.size() <= 1) {
            throw new RuntimeException();
        }
    }

    public void remove(Section it) {
        this.values.remove(it);
    }
}
