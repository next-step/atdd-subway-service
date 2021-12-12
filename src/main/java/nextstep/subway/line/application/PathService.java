package nextstep.subway.line.application;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.infrastructure.line.LineRepository;
import nextstep.subway.line.domain.Path;
import nextstep.subway.line.dto.path.PathResponse;
import nextstep.subway.line.domain.PathResult;
import nextstep.subway.member.domain.Age;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PathService {

    private final FarePolicyHandler subwayFarePolicyHandler;
    private final LineRepository lineRepository;
    private final StationService stationService;
    private final PathSearch pathSearch;

    public PathService(FarePolicyHandler subwayFarePolicyHandler, LineRepository lineRepository,
        StationService stationService, PathSearch pathSearch) {
        this.subwayFarePolicyHandler = subwayFarePolicyHandler;
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.pathSearch = pathSearch;
    }

    @Transactional(readOnly = true)
    public PathResponse getShortestPath(Long source, Long target, Age age) {
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);

        Lines lines = new Lines(lineRepository.findAll());
        Path path = lines.toPath(sourceStation, targetStation, pathSearch);
        PathResult pathSearchResult = path.getShortestPath();

        Fare fare = new Fare(pathSearchResult, age);

        return PathResponse.of(
            StationResponse.toList(pathSearchResult.getResult()),
            pathSearchResult.getWeight(),
            subwayFarePolicyHandler.apply(fare)
        );
    }
}
