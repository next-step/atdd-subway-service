package nextstep.subway.path.domain;

public class FareCalculator {
    private FareCalculator() {
    }
    
    public static int calculator(int distance, int surcharge, int age) {
        return AgeDiscountPolicy.discount(DistanceFareCalculator.calculator(distance) + surcharge, age);
    }

}
