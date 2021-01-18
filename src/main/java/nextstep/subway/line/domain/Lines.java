package nextstep.subway.line.domain;

import nextstep.subway.HibernateUtils;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lines {

    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public PathResponse findPath(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath<>(toGrapth());
        List<Station> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        int distance = IntStream.range(0, shortestPath.size())
                .reduce((accum, idx) -> {
                    Station upStation = shortestPath.get(idx - 1);
                    Station downStation = shortestPath.get(idx);
                    Section section = this.findSection(upStation, downStation);
                    return accum + section.getDistance();
                })
                .getAsInt();
        return new PathResponse(distance, shortestPath.stream().map(StationResponse::new).collect(Collectors.toList()));
    }

    private Section findSection(Station upStation, Station downStation) {
        return this.lines.stream().filter(line -> line.getSections().findSection(upStation, downStation).isPresent())
                .findFirst()
                .get()
                .getSections().findSection(upStation, downStation)
                .get();
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> toGrapth() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        this.lines.forEach(line -> {
            line.getStations().forEach(item -> graph.addVertex(item));
            line.getSections().getSections().forEach(section -> {
                graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
            });
        });
        return graph;
    }

}
