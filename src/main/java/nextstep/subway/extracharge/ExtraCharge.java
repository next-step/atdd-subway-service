package nextstep.subway.extracharge;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ExtraCharge {

    @Column
    private int extraCharge;

    public ExtraCharge() {
    }

    public ExtraCharge(int extraCharge) {
        this.extraCharge = extraCharge;
    }

    public int getExtraCharge() {
        return extraCharge;
    }
}
