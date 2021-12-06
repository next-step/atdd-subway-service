package nextstep.subway.path.infrastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathAnalysisKey;
import nextstep.subway.path.dto.ShortestPathInfo;
import nextstep.subway.path.dto.SubwayWeightedEdge;
import nextstep.subway.station.domain.Station;

public class PathAnalysis {
    private WeightedMultigraph<PathAnalysisKey, SubwayWeightedEdge> graph;
    private DijkstraShortestPath<PathAnalysisKey, SubwayWeightedEdge> shortestPath ;

    private PathAnalysis(Sections sections) {
        vaildateInitialize(sections);
        this.graph = new WeightedMultigraph<>(SubwayWeightedEdge.class);

        for (SubwayWeightedEdge subwayWeightedEdge : generateSubwayWeightedEdges(sections)) {
            this.graph.addVertex(PathAnalysisKey.of(subwayWeightedEdge.getUpStation()));
            this.graph.addVertex(PathAnalysisKey.of(subwayWeightedEdge.getDownStation()));

            if (this.graph.addEdge(PathAnalysisKey.of(subwayWeightedEdge.getDownStation()), PathAnalysisKey.of(subwayWeightedEdge.getUpStation()), subwayWeightedEdge)) {
                this.graph.setEdgeWeight(subwayWeightedEdge, subwayWeightedEdge.getDistance().value());
            }
        }

        this.shortestPath = new DijkstraShortestPath<>(this.graph);
    }

    private List<SubwayWeightedEdge> generateSubwayWeightedEdges(Sections sections) {
        List<SubwayWeightedEdge> realItems = new ArrayList<>();

        List<SubwayWeightedEdge> subwayWeightedEdges = convertSectionToSubwayWeightedEdge(sections);

        realItems.addAll(findUniqueSection(subwayWeightedEdges));
        realItems.addAll(findMinLineExtraFareSecionAboutDuplicateSection(subwayWeightedEdges));

        return realItems;
    }

    private List<SubwayWeightedEdge> findMinLineExtraFareSecionAboutDuplicateSection(List<SubwayWeightedEdge> subwayWeightedEdges) {
        List<SubwayWeightedEdge> duplicateSubwayWeightedEdges = new ArrayList<>();

        subwayWeightedEdges.stream()
                            .filter(item -> Collections.frequency(subwayWeightedEdges, item) > 1)
                            .collect(Collectors.groupingBy(SubwayWeightedEdge::getGroupBy))
                            .values()
                            .forEach(
                                groupedSubwayWeightedEdges -> duplicateSubwayWeightedEdges.add(findMinLineExtraFareSection(groupedSubwayWeightedEdges))
                            );

        return duplicateSubwayWeightedEdges;
    }

    private SubwayWeightedEdge findMinLineExtraFareSection(List<SubwayWeightedEdge> subwayWeightedEdges) {
        return subwayWeightedEdges.stream()
                                    .min(Comparator.comparingInt(line -> line.getLineExtraFare().value()))
                                    .orElseThrow(() -> new NoSuchElementException("라인의 최소 추가금액을 찾을 수 없습니다."));
    }

    private List<SubwayWeightedEdge> convertSectionToSubwayWeightedEdge(Sections sections) {
        return sections.getSections().stream()
                        .map(SubwayWeightedEdge::of)
                        .collect(Collectors.toList());
    }

    private List<SubwayWeightedEdge> findUniqueSection(List<SubwayWeightedEdge> subwayWeightedEdges) {
        List<SubwayWeightedEdge> uniqueSubwayWeightedEdges = new ArrayList<>();

        subwayWeightedEdges.stream()
                            .filter(item -> Collections.frequency(subwayWeightedEdges, item) <= 1)
                            .collect(Collectors.groupingBy(SubwayWeightedEdge::getGroupBy))
                            .values()
                            .forEach(
                                subwayWeightedEdge -> uniqueSubwayWeightedEdges.addAll(subwayWeightedEdge)
                            );

        return uniqueSubwayWeightedEdges;
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
        GraphPath<PathAnalysisKey, SubwayWeightedEdge> graphPath = null;

        try {
            graphPath = this.shortestPath.getPath(PathAnalysisKey.of(source), PathAnalysisKey.of(target));
        } catch (IllegalArgumentException ex) {
            throw new NoSuchElementException("등록된 경로들 중에 찾을 역이 없습니다.");
        }

        if (graphPath == null) {
            throw new NoSuchElementException(String.format("%s->%s 에대한 경로가 조회되지 않습니다.", source.getName(), target.getName()));
        }

        List<Line> lines = getLinesAboutShortestPaths(graphPath);
        return ShortestPathInfo.of(graphPath.getVertexList(), Distance.of((int)graphPath.getWeight()), lines);
    }

    private List<Line> getLinesAboutShortestPaths(GraphPath<PathAnalysisKey, SubwayWeightedEdge> graphPath) {
        return graphPath.getEdgeList().stream()
                        .map(item->item.getLine())
                        .distinct()
                        .collect(Collectors.toList());
    }
}
