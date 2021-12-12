package nextstep.subway.path.domain;

import java.math.BigInteger;

public class Fare {
    private final static BigInteger DEFAULT_FARE = new BigInteger("1250");
    private final BigInteger fare;

    private Fare(BigInteger fare) {
        this.fare = fare;
    }

    public static Fare from(Long distance) {
        BigInteger overCharge= new BigInteger(OverChargeRule.calculateOverFare(distance).toString());
        System.out.println(overCharge);
       return new Fare(DEFAULT_FARE.add(overCharge));
    }



    public BigInteger getFare() {
        return fare;
    }
}
