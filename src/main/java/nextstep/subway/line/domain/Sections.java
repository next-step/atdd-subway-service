package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> values = new ArrayList<>();

    protected Sections() {

    }

    public void add(Section section) {
        this.values.add(section);
    }

    public List<Section> get() {
        return this.values;
    }

    public int size() {
        return values.size();
    }
}
