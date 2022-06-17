package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    public PathResponse findPath(Long source, Long target) {
        List<Station> stations = Arrays.asList(new Station("강남역"), new Station("광교역"));
        return new PathResponse(stations.stream()
                                        .map(StationResponse::of)
                                        .collect(Collectors.toList()), 40);
    }
}
