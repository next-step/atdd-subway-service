package nextstep.subway.path.domain;

import nextstep.subway.path.domain.fee.distanceFee.*;
import org.springframework.stereotype.Component;

@Component
public class FeeCalculatorService {
    DistanceFee calculateDistanceFee(ShortestPath shortestPath) {
        int distance = (int) shortestPath.calculateTotalDistance();

        return DistanceFeeSelector.select(distance);
    }
}
