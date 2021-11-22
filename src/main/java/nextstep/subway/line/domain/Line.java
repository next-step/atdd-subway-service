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
import nextstep.subway.station.domain.Station;

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
        Assert.notNull(name, "이름이 null 일 수 없습니다.");
        Assert.notNull(color, "색상이 null 일 수 없습니다.");
        Assert.notNull(sections, "구간들이 null 일 수 없습니다.");
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public static Line of(Name name, Color color, Sections sections) {
        return new Line(name, color, sections);
    }

    public void update(Name name, Color color) {
        Assert.notNull(name, "수정하는 이름이 null 일 수 없습니다.");
        Assert.notNull(color, "수정하는 색상이 null 일 수 없습니다.");
        this.name = name;
        this.color = color;
    }

    public Long id() {
        return id;
    }

    public Name name() {
        return name;
    }

    public Color color() {
        return color;
    }

    public List<Section> getSections() {
        return sections.getList();
    }

    public List<Station> stations() {
        return sections.stations();
    }

    public void addSection(Section section) {
        sections.add(section);
    }
}
