package nextstep.subway.fares.domain;


public class Fare {

    private int fare;

    public Fare() {
    }

    public void add(int fare) {
        this.fare += fare;
    }

    public void minus(int fare) {
        this.fare -= fare;
    }

    public void discount(double rate) {
        fare *= (1 - rate);
    }

    public int getFare() {
        return fare;
    }
}
