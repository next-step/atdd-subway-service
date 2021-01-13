package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PersonGroup;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final StationService stationService;
    private final LineRepository lineRepository;

    public PathService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPathByIds(int userAge, Long sourceId, Long targetId) {
        if(sourceId.equals(targetId)) {
            throw new IllegalArgumentException("동일한 ID를 입력했습니다.");
        }
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = PathFinder.initialPathFinder(lines);
        GraphPath path = pathFinder.getShortestPath(sourceId, targetId);

        Fare fare = new Fare();
        fare.addFare(FareCalculator.additionalLineFare(lines));
        fare.addFare(FareCalculator.additionalDistanceFare(path.getWeight()));
        fare.minusFare(PersonGroup.discountFarePerAge(userAge, fare.getFare()));

        return PathResponse.of(getStationsById(path.getVertexList()), path.getWeight(), fare.getFare());
    }

    private List<Station> getStationsById(List<Long> ids) {
        return ids.stream()
                .map(id -> stationService.findStationById(id))
                .collect(Collectors.toList());
    }
}
