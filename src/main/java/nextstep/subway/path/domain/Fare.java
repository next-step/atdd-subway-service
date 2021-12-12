package nextstep.subway.path.domain;

import java.math.BigInteger;

public class Fare {
    private final static BigInteger DEFAULT_FARE = new BigInteger("1250");
    private final BigInteger fare;

    public Fare(String textFare) {
        fare = new BigInteger(textFare);
    }

    private Fare(BigInteger fare) {
        this.fare = fare;
    }

    public static Fare from(Long distance) {
       return new Fare(DEFAULT_FARE);
    }

    public BigInteger getFare() {
        return fare;
    }
}
