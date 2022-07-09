package nextstep.subway.fare.policy;

import nextstep.subway.path.domain.Path;

public interface SurchargeCalculator {
    int calculate(Path shortestPath);
}
