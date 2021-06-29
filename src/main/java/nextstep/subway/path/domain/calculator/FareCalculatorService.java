package nextstep.subway.path.domain.calculator;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;

@Service
public class FareCalculatorService {

    private static final int BASE_FARE = 1250;
    private DistanceCalculatorService distanceCalculator;
    private AgeCalculatorService ageCalculator;

    public FareCalculatorService(DistanceCalculatorService distanceCalculator, AgeCalculatorService ageCalculator) {
        this.distanceCalculator = distanceCalculator;
        this.ageCalculator = ageCalculator;
    }

    public int getPrice(int distance, LoginMember member, int aditionalFare) {
        int fare = BASE_FARE + aditionalFare;
        fare += distanceCalculator.getDistanceFare(distance);
        fare *= ageCalculator.getDiscountRate(member);
        return fare;
    }

}
