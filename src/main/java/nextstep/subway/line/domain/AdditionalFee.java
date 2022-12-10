package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class AdditionalFee {
    private Integer fee;

    protected AdditionalFee() {
    }

    public AdditionalFee(Integer fee) {
        this.fee = fee;
    }

    public Integer get() {
        return fee;
    }
}
