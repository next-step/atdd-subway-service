package nextstep.subway.path.application;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.AgePolicy;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.exeption.NotFoundStationException;

@Service
public class PathService {

    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(Long source, Long target, AgePolicy agePolicy) {
        Station start = stationRepository.findById(source).orElseThrow(NotFoundStationException::new);
        Station end = stationRepository.findById(target).orElseThrow(NotFoundStationException::new);
        List<Line> lines = lineRepository.findAll();
        Path path = new Path(lines);

        List<StationResponse> stations = path.findShortestPath(start, end).stream().map(StationResponse::of).collect(toList());
        int minDistance = path.findShortestDistance(start, end);
        long fare = path.findPathFare(start, end, agePolicy).getValue();
        return PathResponse.of(stations, minDistance, fare);
    }
}
