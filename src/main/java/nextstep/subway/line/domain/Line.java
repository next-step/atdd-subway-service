package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import nextstep.subway.common.domain.BaseEntity;
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

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    private Line(Name name, Color color) {
        this.name = name;
        this.color = color;
    }

    public static Line of(Name name, Color color) {
        return new Line(name, color);
    }

    private Line(Name name, Color color, Station upStation, Station downStation, Distance distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public static Line of(Name name, Color color, Station upStation, Station downStation, Distance distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public void update(Name name, Color color) {
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
        return sections;
    }
}
