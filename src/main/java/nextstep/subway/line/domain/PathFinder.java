package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.NoSuchElementException;

public class PathFinder {

    private final DijkstraShortestPath<Station, SectionWeightedEdge> dijkstraShortestPath;

    public PathFinder(Lines lines, Stations stations) {
        WeightedMultigraph<Station, SectionWeightedEdge> graph = new WeightedMultigraph(SectionWeightedEdge.class);

        initStationVertex(stations, graph);
        initSectionEdge(lines, graph);

        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void initSectionEdge(Lines lines, WeightedMultigraph<Station, SectionWeightedEdge> graph) {
        lines.getSections()
                .forEach(section -> {
                    SectionWeightedEdge sectionWeightedEdge = new SectionWeightedEdge(section);
                    graph.addEdge(section.getUpStation(), section.getDownStation(), sectionWeightedEdge);
                    graph.setEdgeWeight(sectionWeightedEdge, section.getDistance());

                });
    }

    private void initStationVertex(Stations stations, WeightedMultigraph<Station, SectionWeightedEdge> graph) {
        stations.getStations()
                .forEach(station -> graph.addVertex(station));
    }

    public List<Station> getPath(Station source, Station target) {
        try {
            return dijkstraShortestPath.getPath(source, target)
                    .getVertexList();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("출발지와 목적지는 연결되어있지 않는 경로입니다.");
        }

    }

    public int getWeight(Station source, Station target) {
        return (int) dijkstraShortestPath.getPath(source, target).getWeight();
    }

    public List<SectionWeightedEdge> getSectionWeightEdge(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getEdgeList();
    }
}
