package nextstep.subway.line.domain;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.util.Assert;

import nextstep.subway.common.domain.BaseEntity;
import nextstep.subway.common.domain.Fare;
import nextstep.subway.common.domain.Name;
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

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "extra_fare", nullable = false))
    private Fare extraFare;

    public Line() {
    }

    private Line(Name name, Color color) {
        this.name = name;
        this.color = color;
    }

    public static Line of(Name name, Color color) {
        return new Line(name, color);
    }

    private Line(Name name, Color color, Sections sections, Fare extraFare) {
        Assert.notNull(name, "이름은 필수입니다.");
        Assert.notNull(color, "색상은 필수입니다.");
        Assert.notNull(sections, "구간들은 필수입니다.");
        this.name = name;
        this.color = color;
        setExtraFare(extraFare);
        setSections(sections);
    }


    private Line(Name name, Color color, Sections sections) {
        this(name, color, sections, Fare.zero());
    }

    private void setSections(Sections sections) {
        sections.setLine(this);
        this.sections = sections;
    }

    private void setExtraFare(Fare extraFare) {
        if (extraFare == null) {
            this.extraFare = Fare.zero();
            return;
        }
        this.extraFare = extraFare;
    }


    public static Line of(Name name, Color color, Sections sections) {
        return new Line(name, color, sections);
    }

    public static Line of(Name name, Color color, Sections sections, Fare extraFare) {
        return new Line(name, color, sections, extraFare);
    }

    public void update(Name name, Color color) {
        Assert.notNull(name, "이름은 필수입니다.");
        Assert.notNull(color, "색상은 필수입니다.");
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
        return sections.list();
    }

    public Integer getExtraFare() {
        return extraFare.value();
    }

	public Stations allStations() {
        return sections.sortedStations();
	}

    public void connectSection(Section section, List<Section> sectionsToUpdate) {
        sections.connect(section, sectionsToUpdate);
        section.updateLine(this);
    }

    public void removeSection(Section sectionByUpStation, Section sectionByDownStation) {
        this.sections.remove(sectionByUpStation, sectionByDownStation);
    }

    public Fare extraFare() {
        return extraFare;
    }
}
