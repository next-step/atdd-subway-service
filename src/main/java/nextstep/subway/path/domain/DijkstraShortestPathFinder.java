package nextstep.subway.path.domain;

import nextstep.subway.common.ErrorMessage;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.exception.NotConnectedException;
import nextstep.subway.path.exception.SameSourceAndTargetException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DijkstraShortestPathFinder implements PathFinderStrategy {

    public Path getShortestDistance(Lines lines, Station source, Station target) {
        List<Section> sections = lines.getAllSections();

        validateSameSourceAndTarget(source, target);
        validateStationExistence(sections, source, target);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        DijkstraShortestPath path = new DijkstraShortestPath(graph);

        generateGraph(sections, graph);

        GraphPath graphPath = path.getPath(source, target);

        List<Station> stations = graphPath.getVertexList();
        int distance = (int) graphPath.getWeight();

        return new Path(stations, distance, lines);
    }

    private void generateGraph(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            graph.addVertex(upStation);
            graph.addVertex(downStation);

            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }

    private void validateSameSourceAndTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new SameSourceAndTargetException(ErrorMessage.SAME_SOURCE_AND_TARGET);
        }
    }

    private void validateStationExistence(List<Section> sectionList, Station source, Station target) {
        Sections sections = new Sections(sectionList);
        if (!sections.hasStation(source) || !sections.hasStation(target)) {
            throw new NotConnectedException(ErrorMessage.NOT_CONNECTED);
        }
    }
}
