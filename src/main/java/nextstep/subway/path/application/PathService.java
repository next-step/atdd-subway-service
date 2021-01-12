package nextstep.subway.path.application;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.NotFoundStationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.PathSelector;
import nextstep.subway.path.domain.PaymentCalculator;
import nextstep.subway.path.domain.ThroughLineSelector;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        PathResult result = selectPath(source, target);

        List<Long> stationIds = result.getStationIds();
        List<StationResponse> shortestStations = toStationResponses(stationIds);
        int totalDistance = result.getTotalDistance();
        int payment = calculatePayment(stationIds, totalDistance);

        return new PathResponse(shortestStations, totalDistance, payment);
    }

    private int calculatePayment(List<Long> stationIds, int distance) {
        return PaymentCalculator.calculatePayment(findThroughLines(stationIds), distance);
    }

    private List<Line> findThroughLines(List<Long> stationIds) {
        List<Line> lines = lineRepository.findAll();
        ThroughLineSelector throughLineSelector = new ThroughLineSelector(lines, stationIds);
        return throughLineSelector.find();
    }

    private List<StationResponse> toStationResponses(List<Long> stationIds) {
        final Map<Long,Station> stations = stationRepository.findAll().stream()
                .collect(Collectors.toMap(Station::getId, it -> it));

        return stationIds.stream()
                .map(stations::get)
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    private PathResult selectPath(Long source, Long target) {
        if (source.equals(target)) {
            throw new BadRequestException("출발역과 도착역은 달라야 합니다");
        }

        Station sourceStation = stationRepository.getOne(source);
        Station targetStation = stationRepository.getOne(target);
        return PathSelector.select(sourceStation, targetStation);
    }
}
