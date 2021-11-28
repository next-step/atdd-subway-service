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
    private WeightedMultigraph<PathAnalysisKey, DefaultWeightedEdge> graph;
    private DijkstraShortestPath<PathAnalysisKey, DefaultWeightedEdge> shortestPath ;

    private PathAnalysis(Sections sections) {
        vaildateInitialize(sections);
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        
        for (Section section : sections.getSections()) {
            this.graph.addVertex(PathAnalysisKey.of(section.getUpStation()));
            this.graph.addVertex(PathAnalysisKey.of(section.getDownStation()));

            DefaultWeightedEdge newEdge = this.graph.addEdge(PathAnalysisKey.of(section.getDownStation()), PathAnalysisKey.of(section.getUpStation()));
            int distance = section.getDistance().value();
            this.graph.setEdgeWeight(newEdge, distance);
        }

        this.shortestPath = new DijkstraShortestPath<>(this.graph);
    }

    public static PathAnalysis of(Sections sections) {
        return new PathAnalysis(sections);
    }

    private void vaildateInitialize(Sections sections) {
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("경로를 분석할 구간이 없습니다.");
        }
    }

    public ShortestPathInfo findShortestPaths(Station source, Station target) {
        GraphPath<PathAnalysisKey, DefaultWeightedEdge> graphPath = null;

        try {
            graphPath = this.shortestPath.getPath(PathAnalysisKey.of(source), PathAnalysisKey.of(target));
        } catch (IllegalArgumentException ex) {
            throw new NoSuchElementException("등록된 경로들 중에 찾을 역이 없습니다.");
        }

         

        if (graphPath == null) {
            throw new NoSuchElementException(String.format("%s->%s 에대한 경로가 조회되지 않습니다.", source.getName(), target.getName()));
        }

        return ShortestPathInfo.of(graphPath.getVertexList(), Distance.of((int)graphPath.getWeight()));
    }
}
