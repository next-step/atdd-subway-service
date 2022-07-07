package nextstep.subway.path.application;

import nextstep.subway.station.exception.StationNotFoundException;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse minPath(Long sourceStationId, Long targetStationId) {
        System.out.println("PathService.minPath");

        Station sourceStation = stationRepository.findById(sourceStationId)
                .orElseThrow(() -> new StationNotFoundException("경로 시작역이 존재하지 않습니다."));
        Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(() -> new StationNotFoundException("경로 도착역이 존재하지 않습니다."));

        Lines lines = new Lines(lineRepository.findAll());
        GraphPath graphPath = lines.minPath(sourceStation, targetStation);

        PathResponse pathResponse = new PathResponse(graphPath.getVertexList(), (int) graphPath.getWeight());

        return pathResponse;
    }
}
