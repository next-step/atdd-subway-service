package nextstep.subway.line.domain;

import io.jsonwebtoken.lang.Assert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.common.domain.Name;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

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

    private Line(Name name, Color color, Sections sections) {
        Assert.notNull(name, "이름은 필수입니다.");
        Assert.notNull(color, "색상은 필수입니다.");
        Assert.notNull(sections, "구간들은 필수입니다.");
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.sections.setLine(this);
    }

    public static Line of(Name name, Color color, Sections sections) {
        return new Line(name, color, sections);
    }

    public void update(Name name, Color color) {
        Assert.notNull(name, "수정하려는 이름은 필수입니다.");
        Assert.notNull(color, "수정하려는 색상은 필수입니다.");
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

    public Stations sortedStations() {
        return sections.sortedStations();
    }

    public void addSection(Section section) {
        sections.add(section);
        section.changeLine(this);
    }

    public void removeStation(Station station) {
        sections.removeStation(station);
    }

    Sections sections() {
        return sections;
    }
}
