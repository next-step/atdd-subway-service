package nextstep.subway.line.domain;

import nextstep.subway.exception.NotValidDataException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static nextstep.subway.exception.type.ValidExceptionType.MIN_EXTRA_FEE_NOT_ZERO;

@Embeddable
public class ExtraFee {

    private static final int MIN_EXTRA_FEE = 0;

    @Column(nullable = false)
    private int extraFee;


    protected ExtraFee() {
    }

    private ExtraFee(int extraFee) {
        validCheckMinDistanceSize(extraFee);
        this.extraFee = extraFee;
    }

    public static ExtraFee from(int extraFee) {
        return new ExtraFee(extraFee);
    }

    public static ExtraFee ofZero() {
        return new ExtraFee(MIN_EXTRA_FEE);
    }

    private void validCheckMinDistanceSize(int extraFee) {
        if (MIN_EXTRA_FEE > extraFee) {
            throw new NotValidDataException(MIN_EXTRA_FEE_NOT_ZERO.getMessage());
        }
    }

    public int getExtraFee() {
        return extraFee;
    }
}
