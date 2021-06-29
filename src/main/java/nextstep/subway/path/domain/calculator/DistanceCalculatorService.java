package nextstep.subway.path.domain.calculator;

import org.springframework.stereotype.Service;

import nextstep.subway.path.domain.Surcharge;

@Service
public class DistanceCalculatorService {

    public int getDistanceFare(int distance) {
        int firstDistance = distance;
        int fare = 0;
        Surcharge surcharge = Surcharge.findSurchargeByDistance(firstDistance);
        fare = addDistanceRangeFare(firstDistance, fare, surcharge);

        return fare;
    }

    private int addDistanceRangeFare(int firstDistance, int fare, Surcharge surcharge) {
        if (surcharge == Surcharge.NONE) {
            return fare;
        }
        int distanceBoundary = surcharge.getDistanceBoundary();
        int addDistance = surcharge.getAddDistance();
        int addFare = surcharge.getAddFare();
        fare += calculateOverFare(firstDistance - distanceBoundary, addDistance, addFare);

        return addDistanceRangeFare(distanceBoundary, fare, Surcharge.findSurchargeByDistance(firstDistance));
    }

    private static int calculateOverFare(int distance, int divisor, int addFare) {
        return (int)((Math.ceil((distance) / divisor)) * addFare);
    }

}
