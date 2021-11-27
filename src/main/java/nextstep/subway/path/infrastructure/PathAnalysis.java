package nextstep.subway.path.infrastructure;

import java.util.NoSuchElementException;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathAnalysisKey;
import nextstep.subway.path.dto.ShortestPathInfo;
import nextstep.subway.station.domain.Station;

public class PathAnalysis {
    private static PathAnalysis pathAnalysis;

    private WeightedMultigraph<PathAnalysisKey, DefaultWeightedEdge> graph;
    private DijkstraShortestPath<PathAnalysisKey, DefaultWeightedEdge> shortestPath ;

    private PathAnalysis() {
    }

    public static PathAnalysis getInstance() {
        if (pathAnalysis == null) {
            pathAnalysis = new PathAnalysis();

            return pathAnalysis;
        }

        return pathAnalysis;
    }
    
    public void initialze(Sections sections) {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.shortestPath = new DijkstraShortestPath<>(this.graph);

        for (Section section : sections.getSections()) {
            addPath(section);
        }
    }

    public void clear() {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.shortestPath = new DijkstraShortestPath<>(this.graph);
    }

    public void addPath(Section section) {
        this.graph.addVertex(PathAnalysisKey.of(section.getUpStation()));
        this.graph.addVertex(PathAnalysisKey.of(section.getDownStation()));

        DefaultWeightedEdge newEdge = this.graph.addEdge(PathAnalysisKey.of(section.getDownStation()), PathAnalysisKey.of(section.getUpStation()));
        int distance = section.getDistance().value();
        this.graph.setEdgeWeight(newEdge, distance);
    }

    public void addAllPath(Sections sections) {
        for (Section section : sections.getSections()) {
            this.addPath(section);
        }
    }

    public void removeAllPath(Sections sections) {
        for (Section section : sections.getSections()) {
            removeSection(section);
        }
    }

    private void removeSection(Section section) {
        this.graph.removeEdge(PathAnalysisKey.of(section.getDownStation()), PathAnalysisKey.of(section.getUpStation()));
    }

    public ShortestPathInfo findShortestPaths(Station source, Station target) {
        GraphPath<PathAnalysisKey, DefaultWeightedEdge> graphPath  = this.shortestPath.getPath(PathAnalysisKey.of(source), PathAnalysisKey.of(target));

        if (graphPath == null) {
            throw new NoSuchElementException(String.format("%s->%s 에대한 경로가 조회되지 않습니다.", source.getName(), target.getName()));
        }

        return ShortestPathInfo.of(graphPath.getVertexList(), Distance.of((int)graphPath.getWeight()));
    }
}
