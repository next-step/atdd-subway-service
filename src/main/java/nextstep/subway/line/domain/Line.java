package nextstep.subway.line.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "extra_charge")
    private int extraCharge = 0;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color, int extraCharge) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraCharge = extraCharge;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(this, upStation, downStation, distance);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int extraCharge) {
        this.name = name;
        this.color = color;
        sections.add(this, upStation, downStation, distance);
        validateExtraCharge(extraCharge);
        this.extraCharge = extraCharge;
    }

    private void validateExtraCharge(final int extraCharge) {
        if (extraCharge < 0) {
            throw new RuntimeException("추가요금은 0 이상이어야 합니다.");
        }
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void update(final Line line) {
        name = line.getName();
        color = line.getColor();
    }

    public void addSection(final Station upStation, final Station downStation, final int distance) {
        sections.add(this, upStation, downStation, distance);
    }

    public void removeStation(final Station station) {
        sections.removeStation(this, station);
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

    public int getExtraCharge() {
        return extraCharge;
    }
}
