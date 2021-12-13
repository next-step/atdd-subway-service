package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Fare {

    private int value;

    public Fare(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
