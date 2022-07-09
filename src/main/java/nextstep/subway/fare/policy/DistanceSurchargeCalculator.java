package nextstep.subway.fare.policy;

import nextstep.subway.path.domain.Path;

public class DistanceSurchargeCalculator implements SurchargeCalculator {

    @Override
    public int calculate(Path shortestPath) {
        int distance = shortestPath.getDistance();
        int distanceSurcharge = 0;
        if (distance > 50) {
            int extraDistance = distance - 50;
            distanceSurcharge += (Math.ceil((double) extraDistance / 8) * 100);
            distance -= extraDistance;
        }

        if (distance <= 50 && distance >= 10) {
            int extraDistance = distance - 10;
            distanceSurcharge += (Math.ceil((double) extraDistance / 5) * 100);
        }

        return distanceSurcharge;
    }
}
