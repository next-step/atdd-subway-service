package nextstep.subway.line.domain;

import com.sun.istack.NotNull;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(nullable = false)
    private String color;
    @Embedded
    private final Sections sections = new Sections();
    @ColumnDefault("0")
    @Column(nullable = false)
    private int surcharge;

    public Line() {
    }

    public Line(String name, String color) {
        this(name, color, 0);
    }

    public Line(String name, String color, int surcharge) {
        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, upStation, downStation, distance, 0);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int surcharge) {
        this.name = name;
        this.color = color;
        this.sections.add(this, upStation, downStation, distance);
        this.surcharge = surcharge;
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

    public List<Station> getStations() {
        return sections.orderedStations();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public Integer getSurcharge() {
        return surcharge;
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        sections.add(this, upStation, downStation, distance);
    }

    public void removeLineStation(Station station) {
        sections.remove(this, station);
    }
}
