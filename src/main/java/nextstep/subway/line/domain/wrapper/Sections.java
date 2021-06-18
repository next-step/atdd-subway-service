package nextstep.subway.line.domain.wrapper;

import nextstep.subway.line.domain.Section;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EntityExistsException;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public static Sections newInstance() {
        return new Sections();
    }

    public void add(final Section section) {
        if (this.sections.contains(section)) {
            throw new EntityExistsException();
        }
        this.sections.add(section);
    }

    public List<Section> toCollection() {
        return sections;
    }
}
