package nextstep.subway.line.domain;

import io.jsonwebtoken.lang.Assert;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.common.domain.Name;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Color color;

    @Embedded
    private Sections sections;

    protected Line() {
    }

    public Line(Name name, Color color, Sections sections) {
        Assert.notNull(name, "name must not be null");
        Assert.notNull(color, "color must not be null");
        Assert.notNull(sections, "sections must not be null");
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public static Line of(Name name, Color color, Sections sections) {
        return new Line(name, color, sections);
    }

    public void update(Name name, Color color) {
        Assert.notNull(name, "updated name must not be null");
        Assert.notNull(name, "updated color must not be null");
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.toString();
    }

    public String getColor() {
        return color.toString();
    }

    public List<Section> getSections() {
        return sections.getList();
    }
}
