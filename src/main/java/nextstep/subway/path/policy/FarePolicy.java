package nextstep.subway.path.policy;

import nextstep.subway.line.domain.Line;

import java.util.Set;

public interface FarePolicy {
    int calculateOverFare(int distance);
    int calculateOverFare(Set<Line> lines, int distance);
}
