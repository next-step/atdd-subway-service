package nextstep.subway.path.domain;

import nextstep.subway.path.dto.PathSection;
import nextstep.subway.path.dto.PathStation;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathBuilder {
    private WeightedMultigraph<PathStation, DefaultWeightedEdge> graph;
    private PathStation source;
    private PathStation target;

    public PathBuilder fromSections(List<PathSection> sections) {
        WeightedMultigraph<PathStation, DefaultWeightedEdge> weightedGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        sections.forEach(section -> {
            PathStation departureStation = section.getDepartureStation();
            PathStation arrivalStation = section.getArrivalStation();
            weightedGraph.addVertex(departureStation);
            weightedGraph.addVertex(arrivalStation);
            weightedGraph.setEdgeWeight(weightedGraph.addEdge(departureStation, arrivalStation), section.getDistance());
        });
        this.graph = weightedGraph;
        return this;
    }

    public PathBuilder withSource(Station sourceStation) {
        this.source = PathStation.of(sourceStation);
        return this;
    }

    public PathBuilder withTarget(Station targetStation) {
        this.target = PathStation.of(targetStation);
        return this;
    }

    public Path build() {
        return new Path(graph, source, target);
    }
}
