package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class AdditionalFare {

    private int additionalFare;

    public AdditionalFare() { }

    private AdditionalFare(int additionalFare) {
        validate(additionalFare);
        this.additionalFare = additionalFare;
    }

    private void validate(int additionalFare) {
        if (additionalFare < 0) {
            throw new IllegalArgumentException("추가요금은 음수가 될 수 없습니다.");
        }
    }

    public static AdditionalFare of(int additionalFare) {
        return new AdditionalFare(additionalFare);
    }

    public int get() {
        return additionalFare;
    }
}
