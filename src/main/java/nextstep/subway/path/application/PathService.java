package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional
@RequiredArgsConstructor
public class PathService {

    //TODO
    public PathResponse searchPath(final PathRequest request) {
        PathResponse pathResponse = new PathResponse(Arrays.asList(
                new Station(1L, "A"),
                new Station(2L, "B"),
                new Station(3L, "C"),
                new Station(4L, "D"),
                new Station(5L, "E")
        ),40);

        return pathResponse;
    }
}
