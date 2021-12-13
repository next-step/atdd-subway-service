package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Fee {

    private int value;

    public Fee(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
