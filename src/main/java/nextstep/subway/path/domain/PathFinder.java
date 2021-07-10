package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.discountpolicy.DiscountPolicy;
import nextstep.subway.path.domain.extrachargepolicy.ExtraChargePolicy;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.*;
import java.util.stream.Collectors;

import static nextstep.subway.path.domain.discountpolicy.DiscountPolicyFactory.*;
import static nextstep.subway.path.domain.extrachargepolicy.ExtraChargeCalculator.*;
import static nextstep.subway.path.domain.extrachargepolicy.ExtraChargeCalculator.DEFAULT_FARE;

public class PathFinder {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final List<Section> sections;

    public PathFinder(List<Line> lines) {
        this.sections = getSectionsByLines(lines);
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        setVertexAndEdgeWeight(sections);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public List<Station> findPaths(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
        }

        return Optional.of(dijkstraShortestPath.getPath(source, target).getVertexList()).orElseThrow(() -> new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다."));
    }

    public int getPathsDistance(Station source, Station target) {
        return (int) dijkstraShortestPath.getPathWeight(source, target);
    }

    public long getFare(Station source, Station target, int age, int distance) {
        List<DefaultWeightedEdge> edgeList = dijkstraShortestPath.getPath(source, target).getEdgeList();
        List<Section> findSections = findSections(edgeList);
        long extraCharge = getMaxExtraCharge(findSections);
        return calculateFare(extraCharge, getDiscountPolicy(age), getExtraChargePolicy(distance));
    }

    private long calculateFare(long extraCharge, DiscountPolicy discountPolicy, ExtraChargePolicy extraChargePolicy) {
        return discountPolicy.calculate(DEFAULT_FARE + extraChargePolicy.calculateOverFare() + extraCharge);
    }

    private long getMaxExtraCharge(List<Section> findSections) {
        return findSections.stream()
                .mapToLong(section -> section.getLine().getExtraCharge())
                .max()
                .orElse(0L);
    }

    private List<Section> findSections(List<DefaultWeightedEdge> edgeList) {
        List<Section> findSections = new ArrayList<>();
        for (DefaultWeightedEdge edge : edgeList) {
            Section findSection = findSection(edge);
            findSections.add(findSection);
        }
        return findSections;
    }

    private Section findSection(DefaultWeightedEdge edge) {
        return sections.stream()
                .filter(section -> section.isSameUpStation(graph.getEdgeSource(edge)) && section.isSameDownStation(graph.getEdgeTarget(edge)))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("일치하는 구간이 없습니다."));
    }

    private void setVertexAndEdgeWeight(List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private List<Section> getSectionsByLines(List<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
    }
}
