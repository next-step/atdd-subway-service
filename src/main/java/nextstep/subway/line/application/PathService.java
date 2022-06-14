package nextstep.subway.line.application;

import nextstep.subway.exception.NotExistStationException;
import nextstep.subway.exception.SameOriginDestinationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        if (source.equals(target)) {
            throw new SameOriginDestinationException("출발지와 도착지가 같습니다.");
        }
        List<Line> lines = lineRepository.findAll();

        Station sourceStation = stationRepository.findById(source)
                .orElseThrow(() -> new NotExistStationException("출발역이 없습니다."));

        Station targetStation = stationRepository.findById(target)
                .orElseThrow(() -> new NotExistStationException("도착역이 없습니다."));

        return PathFinder.of().findShortest(lines, sourceStation, targetStation);
    }
}
