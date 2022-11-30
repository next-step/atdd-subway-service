package nextstep.subway.path.application;

import org.springframework.stereotype.Component;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.PathFinder;

@Component
public class PathFinderGenerator {

    public PathFinder generate(Lines lines) {
        return new PathFinder(lines);
    }
}
