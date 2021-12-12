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

    @Embedded
//    private ExtraCharge extraCharge = ExtraCharge.of(ExtraCharge.NO_FARE);
    private Money extraCharge = Money.of(Money.MIN_VALUE);

    @Embedded
    private final Sections sections = new Sections();

    protected Line() {
    }

    private Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    private Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        addSection(Section.of(this, upStation, downStation, Distance.of(distance)));
    }

    private Line(String name, String color, Station upStation, Station downStation, int distance, int extraCharge) {
        this.name = name;
        this.color = color;
        this.extraCharge = Money.of(extraCharge);
        addSection(Section.of(this, upStation, downStation, Distance.of(distance)));
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance, int extraCharge) {
        return new Line(name, color, upStation, downStation, distance, extraCharge);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeStation(Long stationId) {
        sections.remove(stationId);
    }

    public boolean useCharge() {
        return extraCharge.exist();
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

    public Sections sections() {
        return sections;
    }

    public List<Section> getSections() {
        return sections.getList();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extraCharge() {
        return extraCharge.intValue();
    }
}
