package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Surcharge {

    private int surcharge;

    protected Surcharge() {
    }

    public Surcharge(int surcharge) {
        this.validate(surcharge);
        this.surcharge = surcharge;
    }

    public int getSurcharge() {
        return surcharge;
    }

    private void validate(int surcharge) {
        if(surcharge < 0) {
            throw new IllegalArgumentException("노선 추가 요금은 0보다 작을 수 없습니다.");
        }
    }
}
