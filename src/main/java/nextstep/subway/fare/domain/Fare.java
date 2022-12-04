package nextstep.subway.fare.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare {
    public static final int BASIC_FARE = 1_250;
    public static final int FIRST_SECTION_MAX_SURCHARGE = 800;

    @Column(nullable = false)
    private int fare;

    protected Fare() {}

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare from(int fare) {
        return new Fare(fare);
    }

    public Fare add(Fare fare) {
        return new Fare(this.fare + fare.fare);
    }

    public Fare subtract(Fare fare) {
        return new Fare(this.fare - fare.fare);
    }

    public int value() {
        return fare;
    }
}
