package nextstep.subway.line.domain;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = new LinkedList(sections);
    }

    public List<Section> value() {
        return this.sections;
    }

}
