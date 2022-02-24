package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.AgeFareDiscount;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(LoginMember loginMember, Long source, Long target) {
        Station start = station(source);
        Station end = station(target);
        PathResult result = path().getShortestPath(start, end);
        int discountedFare = AgeFareDiscount.getAgeDiscountedFare(loginMember.getAge(), result.getFare());
        return PathResponse.of(stationResponses(result.getStations()), result.getDistance(), discountedFare);
    }

    private Path path() {
        return Path.of(lineRepository.findAll());
    }

    private Station station(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("지하철 역이 존재하지 않습니다."));
    }

    private List<StationResponse> stationResponses(List<Station> stations) {
        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }


}
