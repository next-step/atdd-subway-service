package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Surcharge {
    @Column
    private int surcharge;

    protected Surcharge() {
    }

    public Surcharge(int surcharge) {
        this.surcharge = surcharge;
    }

    public static Surcharge nonSurcharge() {
        return new Surcharge(0);
    }

    public void changeSurcharge(int surcharge) {
        this.surcharge = surcharge;
    }

    public int getSurcharge() {
        return surcharge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Surcharge surcharge1 = (Surcharge) o;
        return surcharge == surcharge1.surcharge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(surcharge);
    }
}
