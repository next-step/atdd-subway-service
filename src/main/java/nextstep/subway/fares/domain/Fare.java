package nextstep.subway.fares.domain;


public class Fare {

    private int fare;

    public Fare() {
    }

    public void add(int fare) {
        this.fare += fare;
    }

    public int getFare() {
        return fare;
    }
}
