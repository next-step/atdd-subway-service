package nextstep.subway.path.domain.policy.fare.line;

import nextstep.subway.line.domain.Line;

import java.util.List;

public interface OverFareByLineStrategy {
    int calculateOverFare(List<Line> lines);
}
