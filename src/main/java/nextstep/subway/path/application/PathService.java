package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.ShortestPathRequest;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public ShortestPathResponse getShortestPath(ShortestPathRequest requestDto) {
        Station starting = stationService.findStationById(requestDto.getStartingStationsId());
        Station destination = stationService.findStationById(requestDto.getDestinationStationsId());

        List<Line> allLines = lineService.findAllLines();

        WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph = new WeightedMultigraph(DefaultWeightedEdge.class);

        allLines.forEach(line -> {
            line.getSections()
                    .toList()
                    .forEach(section -> {
                        stationGraph.addVertex(section.getUpStation());
                        stationGraph.addVertex(section.getDownStation());
                        stationGraph.setEdgeWeight(stationGraph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().toInt());
                    });
        });

        return new ShortestPathResponse();
    }
}
