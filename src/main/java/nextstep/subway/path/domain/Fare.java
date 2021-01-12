package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;

import java.util.List;

public class Fare {
    private static final long INITIAL_FEE = 1250;
    private long fare;
    private static long additionalFare = 0L;

    private Fare() {
        this.fare = INITIAL_FEE;
    }
    
    public static Fare initialFare(List<Line> lines) {
        Fare fare = new Fare();
        lines.forEach(line -> 
                additionalFare = line.getAdditionalFare() > additionalFare ?
                        line.getAdditionalFare() : additionalFare
        );
        return fare;
    }

    public long getFare() {
        return fare;
    }

    public void addFee(long additionalFee) {
        fare += additionalFee;
    }

    public void discountPerAge(Integer age) {
        if(Passenger.TEENAGER.equals(Passenger.checkAge(age))) {
            fare = (long) fare - ((fare - 350) * 20 / 100);
        }
        if(Passenger.CHILD.equals(Passenger.checkAge(age))) {
            fare = (long) fare - ((fare - 350) * 50 / 100);
        }
    }
    
    public void calculateFare(double distance) {
        addFee(additionalFare);
        if(distance - 10 > 0) {
            addFee(excessFare((int) distance - 10));
        }
    }

    private long excessFare(int distance) {
        if(distance > 40) {
            return (long) ((Math.ceil((distance - 1) / 8) + 1) * 100);
        }
        return (long) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
