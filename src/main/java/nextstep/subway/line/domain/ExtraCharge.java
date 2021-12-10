package nextstep.subway.line.domain;

import nextstep.subway.line.exception.fare.IllegalFareException;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : Fare
 * author : haedoang
 * date : 2021/12/10
 * description :
 */
@Embeddable
public class ExtraCharge {
    @Transient
    public static final int NO_FARE = 0;

    private Integer extraCharge = NO_FARE;

    protected ExtraCharge() {
    }

    private ExtraCharge(int extraCharge) {
        validate(extraCharge);
        this.extraCharge = extraCharge;
    }

    public static ExtraCharge of(int extraCharge) {
        return new ExtraCharge(extraCharge);
    }

    private void validate(int extraCharge) {
        if (extraCharge < NO_FARE) {
            throw new IllegalFareException();
        }
    }

    public boolean useCharge() {
        return extraCharge > NO_FARE;
    }

    public int value() {
        return extraCharge;
    }
}