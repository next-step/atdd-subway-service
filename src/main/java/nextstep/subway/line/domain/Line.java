package nextstep.subway.line.domain;

import java.util.*;

import javax.persistence.*;

import org.jgrapht.graph.*;

import nextstep.subway.*;
import nextstep.subway.line.dto.*;
import nextstep.subway.path.domain.*;
import nextstep.subway.station.domain.*;

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

    private int extraFare;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
        this.name = name;
        this.color = color;
        sections.addSection(this, upStation, downStation, distance);
        this.extraFare = 0;
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance, int extraFare) {
        this.name = name;
        this.color = color;
        sections.addSection(this, upStation, downStation, distance);
        this.extraFare = extraFare;
    }

    public static Line of(String name, String color, Station upStation, Station downStation, Distance distance) {
        return new Line(name, color, upStation, downStation, distance, 0);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, Distance distance,
        int extraFare) {
        return new Line(name, color, upStation, downStation, distance, extraFare);
    }

    public static Line from(LineRequest lineRequest) {
        return new Line(lineRequest.getName(), lineRequest.getColor());
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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

    public int getExtraFare() {
        return extraFare;
    }

    public List<Station> stations() {
        return sections.stations();
    }

    public void removeLineStation(Station station) {
        sections.removeLineStation(this, station);
    }

    public void addSection(Station upStation, Station downStation, Distance distance) {
        sections.addSection(this, upStation, downStation, distance);
    }

    public Sections sections() {
        return sections;
    }

    public List<Section> sectionsList() {
        return sections.sections();
    }

    public void addVertexTo(WeightedMultigraph<Station, WeightedEdgeWithLine> graph) {
        stations().forEach(graph::addVertex);
    }

    public void setEdgeWeight(WeightedMultigraph<Station, WeightedEdgeWithLine> graph) {
        sectionsList().forEach(
            section -> {
                WeightedEdgeWithLine weightedEdgeWithLine = WeightedEdgeWithLine.from(this);
                graph.addEdge(section.getUpStation(), section.getDownStation(), weightedEdgeWithLine);
                graph.setEdgeWeight(weightedEdgeWithLine, section.distance());
            }
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Line))
            return false;
        Line line = (Line)o;
        return Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}
