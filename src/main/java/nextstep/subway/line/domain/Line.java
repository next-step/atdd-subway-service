package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.auth.domain.Money;
import nextstep.subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private SectionLineUp sectionLineUp = new SectionLineUp();

    @Embedded
    private LineMoney extraCharge;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
        this.name = name;
        this.color = color;
        sectionLineUp.add(new Section(this, upStation, downStation, distance));
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance,
            LineMoney extraCharge) {
        this(name, color, upStation, downStation, distance);
        this.extraCharge = extraCharge;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
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

    public List<Station> getStations() {
        return sectionLineUp.getStations();
    }

    public void addSection(Section section) {
        sectionLineUp.add(section);
    }

    public void removeStation(Station station) {
        sectionLineUp.remove(station);
    }

    public List<Section> getSections() {
        return sectionLineUp.getSections();
    }

    public Money getExtraCharge() {
        return extraCharge.getMoney();
    }
}
