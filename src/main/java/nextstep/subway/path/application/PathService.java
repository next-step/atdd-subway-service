package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.AgeFareDiscount;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private StationService stationService;
    private LineRepository lineRepository;

    public PathService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(LoginMember loginMember, Long source, Long target) {
        Station start = stationService.findById(source);
        Station end = stationService.findById(target);
        PathResult result = path().getShortestPath(loginMember, start, end);
        return PathResponse.of(stationResponses(result.getStations()), result.getDistance(), result.getFare());
    }

    private Path path() {
        return Path.of(lineRepository.findAll());
    }


    private List<StationResponse> stationResponses(List<Station> stations) {
        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }


}
