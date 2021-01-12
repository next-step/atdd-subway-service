package nextstep.subway.path.domain;

public class Fare {
    private final long INITIAL_FEE = 1250;
    private final Integer TEENAGER_START = 13;
    private final Integer TEENAGER_END = 19;
    private final Integer CHILDHOOD_START = 6;
    private long fare;

    public Fare() {
        this.fare = INITIAL_FEE;
    }

    public long getFare() {
        return fare;
    }

    public void addFee(long additionalFee) {
        fare += additionalFee;
    }

    public void discountPerAge(Integer age) {
        if(TEENAGER_START <= age && age < TEENAGER_END) {
            fare = (long) fare - ((fare - 350) * 20 / 100);
        }
        if(CHILDHOOD_START <= age && age < TEENAGER_START) {
            fare = (long) fare - ((fare - 350) * 50 / 100);
        }
    }
}
