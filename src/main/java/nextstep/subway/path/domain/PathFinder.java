package nextstep.subway.path.domain;

import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {

    public PathResponse ofPathResponse(PathRequest pathRequest) {
        return new PathBuilder()
                .fromSections(pathRequest.getPathSections())
                .withSource(pathRequest.getDepartureStation())
                .withTarget(pathRequest.getArrivalStation())
                .build().ofResponse();
    }
}