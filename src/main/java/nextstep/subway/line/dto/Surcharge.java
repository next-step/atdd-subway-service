package nextstep.subway.line.dto;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Surcharge {
    private BigDecimal surcharge = BigDecimal.ZERO;

    protected Surcharge() {
    }

    public Surcharge(BigDecimal surcharge) {
        this.surcharge = surcharge;
    }

    public BigDecimal value() {
        return this.surcharge;
    }


}
