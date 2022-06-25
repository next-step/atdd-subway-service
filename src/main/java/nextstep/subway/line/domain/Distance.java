package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final String ERR_INPUT_MORE_CLOSER_DISTANCE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    @Column(name = "distance")
    private int length;

    protected Distance() {
        this.length = 0;
    }

    public Distance(int length) {
        validLength(length);
        this.length = length;
    }

    public static Distance empty() {
        return new Distance();
    }

    private void validLength(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("길이는 0 이상으로 입력해야합니다.");
        }
    }

    public int getLength() {
        return length;
    }

    public void lengthMinus(int newLength) {
        validCompareLength(newLength);
        length -= newLength;
    }

    public void validCompareLength(int newLength) {
        if (length <= newLength) {
            throw new IllegalArgumentException(ERR_INPUT_MORE_CLOSER_DISTANCE);
        }
    }

    public boolean isEmpty() {
        return length == 0;
    }
}
