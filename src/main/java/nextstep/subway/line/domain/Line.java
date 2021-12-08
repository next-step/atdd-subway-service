package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
        this.name = name;
        this.color = color;
        sections.addSection(this, upStation, downStation, distance);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, Distance distance) {
        return new Line(name, color, upStation, downStation, distance);
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

    public void addVertexTo(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        stations().forEach(graph::addVertex);
    }

    public void setEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        sectionsList().forEach(section -> graph.setEdgeWeight(
            graph.addEdge(section.getUpStation(), section.getDownStation()),
            section.distance())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Line))
            return false;
        Line line = (Line) o;
        return Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}
