package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AdditionalFare {
    @Column
    private int additionalFare;

    public AdditionalFare(int additionalFare) {
        validate(additionalFare);
        this.additionalFare = additionalFare;
    }

    protected AdditionalFare() {
    }

    public void changeAdditionalFare(int additionalFare) {
        this.additionalFare = additionalFare;
    }

    public int getAdditionalFare() {
        return additionalFare;
    }

    private void validate(int additionalFare) {
        if(additionalFare < 0) {
            throw new IllegalArgumentException("추가요금은 0원 이상이어야합니다.");
        }
    }
}
