package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

import static nextstep.subway.common.domain.BizExceptionMessages.PATH_SAME_SOURCE_WITH_TARGET;
import static nextstep.subway.common.domain.BizExceptionMessages.PATH_UNNORMAL_STATION;

public class PathFinder {
    private final WeightedMultigraph<Station, SectionEdge> graph;

    public PathFinder() {
        this.graph = new WeightedMultigraph<>(SectionEdge.class);
    }

    public Path find(Sections sections, Station sourceStation, Station targetStation) {
        validatePath(sections, sourceStation, targetStation);

        fillVerticesAndEdges(sections);
        return findShortestPath(sourceStation, targetStation);
    }

    private void validatePath(Sections sections, Station sourceStation, Station targetStation) {
        if (sourceStation.isSame(targetStation)) {
            throw new IllegalArgumentException(PATH_SAME_SOURCE_WITH_TARGET.message());
        }
        if (!sections.isEnroll(sourceStation) || !sections.isEnroll(targetStation)) {
            throw new IllegalArgumentException(PATH_UNNORMAL_STATION.message());
        }
    }

    private void fillVerticesAndEdges(Sections sections) {
        for (Section section : sections.values()) {
            SectionEdge sectionEdge = SectionEdge.of(section);

            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistance());
        }
    }

    private Path findShortestPath(Station sourceStation, Station targetStation) {
        GraphPath path = new DijkstraShortestPath(graph).getPath(sourceStation, targetStation);
        List<SectionEdge> sectionEdges = path.getEdgeList();
        List<Station> stationPaths = path.getVertexList();
        Integer distance = (int) path.getWeight();

        return Path.of(sectionEdges, stationPaths, distance);
    }
}
