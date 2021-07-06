package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    public static List<Station> findShortest(Station source, Station target, List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            List<Section> sections = line.getSections();

            for (Section section : sections) {
                Station upStation = section.getUpStation();
                Station downStation = section.getDownStation();

                graph.addVertex(upStation);
                graph.addVertex(downStation);
                graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
            }
        }

        return new DijkstraShortestPath<>(graph).getPath(source, target).getVertexList();
    }
}
