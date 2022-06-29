package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Charge {

    @Column
    private int value;

    protected Charge() {
    }

    public Charge(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
}
