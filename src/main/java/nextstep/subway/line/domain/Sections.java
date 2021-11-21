package nextstep.subway.line.domain;

import io.jsonwebtoken.lang.Assert;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.common.domain.Name;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        orphanRemoval = true)
    private List<Section> list = new ArrayList<>();

    protected Sections() {
    }

    private Sections(Section section) {
        Assert.notNull(section, "initial section must not be null");
        this.list.add(section);
    }

    public static Sections from(Section section) {
        return new Sections(section);
    }

    public List<Section> getList() {
        return list;
    }
}
