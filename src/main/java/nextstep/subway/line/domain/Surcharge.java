package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Surcharge {
    private int surcharge;

    public static Surcharge nonSurcharge() {
        return new Surcharge(0);
    }

    protected Surcharge() {
    }

    public Surcharge(int surcharge) {
        this.surcharge = surcharge;
    }
}
