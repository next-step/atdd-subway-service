package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class ExtraFare {
    private static final int ZERO_FARE = 0;
    private int extraFare = ZERO_FARE;

    protected ExtraFare() {
    }

    public ExtraFare(int extraFare) {
        validateFare(extraFare);
        this.extraFare = extraFare;
    }

    public int getValue() {
        return extraFare;
    }

    private void validateFare(int fare) {
        if (fare < ZERO_FARE) {
            throw new IllegalArgumentException("노선별 추가 요금은 0 이상의 값을 입력해야 합니다.");
        }
    }
}
