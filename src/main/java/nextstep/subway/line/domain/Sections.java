package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public static Sections of() {
        return new Sections();
    }

    public void add(Section section) {
        sections.add(section);
    }

    public Integer count() {
        return sections.size();
    }

    public List<Section> getSections() {
        return sections;
    }
}
