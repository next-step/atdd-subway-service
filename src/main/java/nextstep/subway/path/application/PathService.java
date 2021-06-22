package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.domain.SubwayMapData;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PathService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        List<Line> lines = lineRepository.findAll();
        Station source = findStation(pathRequest.getSource());
        Station target = findStation(pathRequest.getTarget());
        SubwayMapData subwayMapData = new SubwayMapData(lines, SectionEdge.class);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(subwayMapData.initData());

        return PathResponse.of(dijkstraShortestPath.getPath(source, target).getVertexList(),
                (int)dijkstraShortestPath.getPathWeight(source, target));
    }

    private Station findStation(Long id) {
        return stationRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
