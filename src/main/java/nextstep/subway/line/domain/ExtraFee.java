package nextstep.subway.line.domain;

import nextstep.subway.ErrorMessage;

import javax.persistence.Embeddable;

@Embeddable
public class ExtraFee {

    private static int MINIMUM_FEE = 0;

    private int extraFee;

    public ExtraFee() {
        this.extraFee = 0;
    }

    public ExtraFee(int extraFee) {
        if(extraFee < MINIMUM_FEE) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_EXTRA_FEE.getMessage());
        }
        this.extraFee = extraFee;
    }

    public ExtraFee add(ExtraFee addedExtraFee) {
        this.extraFee += addedExtraFee.getExtraFee();
        return this;
    }

    public int getExtraFee() {
        return extraFee;
    }

}
