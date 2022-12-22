package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ExtraFare {

    @Column
    private Double extraFare;

    public ExtraFare() {
        extraFare = (double) 0;
    }

    public ExtraFare(Double extraFare) {
        this.extraFare = extraFare;
    }

    public double getExtraFare() {
        return extraFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExtraFare extraFare1 = (ExtraFare) o;
        return Double.compare(extraFare1.extraFare, extraFare) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(extraFare);
    }

    public int comparTo(ExtraFare e) {
        return Double.compare(this.extraFare, e.extraFare);
    }
}
