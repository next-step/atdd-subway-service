package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>();

    protected Sections() {

    }

    public List<Section> getSections() {
        return new ArrayList<>(sectionList);
    }

    public void add(Section section) {
        sectionList.add(section);
    }

    public void remove(Section section) {
        sectionList.remove(section);
    }

}
