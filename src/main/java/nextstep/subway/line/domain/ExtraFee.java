package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class ExtraFee {
    private int fee;

    protected ExtraFee() {
    }

    public ExtraFee(int fee) {
        this.fee = fee;
    }

    public int getFee() {
        return fee;
    }
}
