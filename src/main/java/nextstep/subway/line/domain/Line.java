package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

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

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
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
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        Stations stations = Stations.from(sections);

        return stations.getStations();
    }

    public Sections getSections() {
        return sections;
    }

    public void addLineStation(SectionRequest request, Station upStation, Station downStation) {
        Section section = new Section(this, upStation, downStation, request.getDistance());
        sections.requestAddSection(section);
    }

    public void removeLineStation(Station station) {
        sections.removeLineStation(station, this);
    }

    public void addPathInfo(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        addGraphVertex(graph);
        sections.addGraphEdge(graph);
    }

    private void addGraphVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Station> stations = getStations();
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }
}
