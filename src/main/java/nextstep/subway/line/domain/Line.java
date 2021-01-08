package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Collections;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private int overFare;

    @Embedded
    private Sections section = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.section.create(new Section(this, upStation, downStation, distance));
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int overFare) {
        this.name = name;
        this.color = color;
        this.overFare = overFare;
        this.section.create(new Section(this, upStation, downStation, distance));
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

    public int getOverFare() {
        return overFare;
    }

    public List<Station> getStations() {
        if (section.isEmpty()) {
            return Collections.emptyList();
        }
        return section.getStations();
    }

    public void add(Station upStation, Station downStation, int distance) {
        section.add(new Section(this, upStation, downStation, distance));
    }

    public void removeStation(Station station) {
        if (section.isRemovable()) {
            throw new IllegalArgumentException("구간 삭제 실패됨");
        }
        section.removeStation(station);
    }

    public List<Section> getSection() {
        return this.section.getSections();
    }

    public boolean hasSection(long source, long target) {
        return this.section.hasSection(source, target);
    }
}
