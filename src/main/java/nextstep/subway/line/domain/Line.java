package nextstep.subway.line.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "surcharge"))
    private Fare surcharge = Fare.wonOf(0);

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(Long id) {
        this.id = id;
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, int surcharge) {
        this.name = name;
        this.color = color;
        this.surcharge = Fare.wonOf(surcharge);
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int surcharge) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
        this.surcharge = Fare.wonOf(surcharge);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.surcharge = line.getSurcharge();
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

    public Fare getSurcharge() {
        return surcharge;
    }

    public int getSurchargeAmount() {
        return surcharge.getAmount();
    }

    public void addSection(Section section) {
        section.toLine(this);
        sections.add(section);
    }

    public Sections getSections() {
        return sections;
    }

    public void deleteStation(Station station) {
        sections.delete(station);
    }
}
