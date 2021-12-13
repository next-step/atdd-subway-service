package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.jgrapht.graph.builder.GraphBuilder;

import nextstep.subway.path.dto.PathDto;
import nextstep.subway.path.dto.PathDtos;
import nextstep.subway.station.domain.Station;

public class StationGraph {
    private final Graph<Station, DefaultWeightedEdge> graph;

    public StationGraph(PathDtos paths) {
        this.graph = makeGraph(paths.getPathDtos());
    }

    private Graph<Station, DefaultWeightedEdge> makeGraph(List<PathDto> paths) {
        GraphBuilder<Station, DefaultWeightedEdge, WeightedMultigraph<Station, DefaultWeightedEdge>> graphBuilder =
            new GraphBuilder<>(new WeightedMultigraph<>(DefaultWeightedEdge.class));

        paths
            .forEach(path -> {
                graphBuilder.addVertex(path.getSource());
                graphBuilder.addVertex(path.getTarget());
                graphBuilder.addEdge(path.getSource(), path.getTarget(), path.getDistanceValue());
            });

        return graphBuilder.buildAsUnmodifiable();
    }

    public DijkstraShortestPath<Station, DefaultWeightedEdge> getDijkstraShortestPath() {
        return new DijkstraShortestPath<>(graph);
    }
}
