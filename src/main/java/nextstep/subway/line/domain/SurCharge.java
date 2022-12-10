package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.ErrorMessage;

@Embeddable
public class SurCharge {

    @Column(name = "sur_charge", nullable = false)
    private int surCharge;

    protected SurCharge() {

    }
    public SurCharge(final int surCharge) {
        if(surCharge<0){
            throw new IllegalArgumentException(ErrorMessage.PRICE_CANNOT_BE_NEGATIVE);
        }
        this.surCharge = surCharge;
    }


    public int value() {
        return surCharge;
    }
}
