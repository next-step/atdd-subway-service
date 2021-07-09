package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OverFare {
    @Column
    private int overFare;

    protected OverFare() {
    }

    public OverFare(int overFare) {
        verifyAvailable(overFare);
        this.overFare = overFare;
    }

    private void verifyAvailable(int extraFare) {
        if (extraFare < 0) {
            throw new IllegalArgumentException("추가비용은 0 이상이어야 합니다.");
        }
    }

    public int value() {
        return overFare;
    }
}
