package nextstep.subway.path.application;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationRepository.findById(source).orElseThrow(() -> new NotFoundException("역이 없습니다."));
        Station targetStation = stationRepository.findById(target).orElseThrow(() -> new NotFoundException("역이 없습니다."));


        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Line line : lines) {
            for (Station station : line.getStationsInOrder()) {
                graph.addVertex(station);
            }
        }
        for (Line line : lines) {
            for (Section section : line.getSections()) {
                graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
            }
        }

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<Station> shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
        int distance = (int) dijkstraShortestPath.getPath(sourceStation, targetStation).getWeight();
        return new PathResponse(shortestPath, distance);
    }
}
