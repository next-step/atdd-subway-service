package nextstep.subway.path.domain;

import static nextstep.subway.common.constants.FareConstants.*;

import java.util.Objects;

import javax.persistence.Embeddable;

import nextstep.subway.path.exception.MinFareException;

@Embeddable
public class Fare {

    private long fare;

    public Fare() {
        fare = MIN_FARE;
    }

    public Fare(long fare) {
        if (fare < MIN_FARE) {
            throw new MinFareException(MIN_FARE);
        }
        this.fare = fare;
    }

    public static int compare(Fare source, Fare target) {
        return (source.fare < target.fare) ? -1 : ((source == target) ? 0 : 1);
    }

    public long getValue() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Fare fare1 = (Fare)o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    public Fare add(Fare extraFare) {
        return new Fare(this.fare + extraFare.fare);
    }

    public Fare minus(Fare minus) {
        return new Fare(this.fare - minus.fare);
    }

    public Fare rate(double rate) {
        long ratedFare = (long)(this.fare * rate);
        return new Fare(ratedFare);
    }
}
