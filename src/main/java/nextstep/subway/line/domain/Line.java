package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private int additionalFee;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color, int additionalFee) {
        this.name = name;
        this.color = color;
        this.additionalFee = additionalFee;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int additionalFee) {
        this.name = name;
        this.color = color;
        this.additionalFee = additionalFee;
        addSection(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections.sections();
    }

    public int getAdditionalFee() {
        return additionalFee;
    }

    public void addSection(Section section) {
        section.applyLine(this);
        sections.addSection(section);
    }

    public void removeSection(Station station) {
        sections.removeSection(station);
    }

    public List<Station> stations() {
        return sections.stations();
    }
}
