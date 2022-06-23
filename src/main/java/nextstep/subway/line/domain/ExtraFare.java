package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ExtraFare {
    @Column
    private Integer extraFare;

    public ExtraFare() {
    }

    public ExtraFare(Integer extraFare) {
        this.extraFare = extraFare;
    }

    public Integer getExtraFare() {
        return extraFare;
    }
}
