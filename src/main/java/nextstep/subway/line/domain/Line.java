package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    private int addPrice;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    Line(int addPrice, Sections sections) {
        this.addPrice = addPrice;
        this.sections = sections;
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, int addPrice) {
        this(name, color);
        this.addPrice = addPrice;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections.addSection(Section.of(this, upStation, downStation, distance));
    }

    public static Line of() {
        return new Line();
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        Line line = new Line(name, color);
        Section section = Section.of(line, upStation, downStation, distance);
        line.sections.addSection(section);
        return line;
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance, int addPrice) {
        Line line = new Line(name, color, addPrice);
        Section section = Section.of(line, upStation, downStation, distance);
        line.sections.addSection(section);
        return line;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public void addSection(Section section) {
        this.sections.addSection(section);
    }

    public void remove(Station station) {
        this.sections.remove(this, station);
    }

    public int getAddPriceIfContains(List<Station> stations) {
        if (sections.isContains(stations)) {
            return this.addPrice;
        }
        return 0;
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

    public int getAddPrice() {
        return addPrice;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }
}
