package nextstep.subway.line.domain;

import nextstep.subway.ErrorMessage;

import javax.persistence.Embeddable;

@Embeddable
public class ExtraFare {

    private static int MINIMUM_FEE = 0;

    private int extraFee;

    public ExtraFare() {
        this.extraFee = 0;
    }

    public ExtraFare(int extraFee) {
        if(extraFee < MINIMUM_FEE) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_EXTRA_FEE.getMessage());
        }
        this.extraFee = extraFee;
    }

    public ExtraFare max(ExtraFare other) {
        return extraFee < other.getValue() ? other : this;
    }

    public int getValue() {
        return extraFee;
    }

}
