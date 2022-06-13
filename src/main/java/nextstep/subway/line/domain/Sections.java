package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>();

    public Sections() {
    }

    public Sections(Section section) {
        this.sectionList.add(section);
    }

    public List<Section> getAll() {
        return this.sectionList;
    }

    public void add(Section section) {
        this.sectionList.add(section);
    }
}
