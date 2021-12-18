package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public Line(final String name, final String color, final Station upStation, final Station downStation, final int distance) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(Section.of(this, upStation, downStation, Distance.of(distance)));
    }

    public static Line of(final LineRequest request, final Station upStation, final Station downStation) {
        return new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
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

    public Sections getSections() {
        return sections;
    }

    public void addSection(Section section) {
        this.sections.addSection(section);
    }

    public void deleteStation(final Station station) {
        this.sections.deleteStation(station);
    }

}
