package nextstep.subway.common.domain;

import io.jsonwebtoken.lang.Assert;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Age {

    @Column(name = "age", nullable = false)
    private int value;

    protected Age() {
    }

    private Age(int value) {
        Assert.isTrue(positive(value), "나이는 반드시 양수이어야 합니다.");
        this.value = value;
    }

    public static Age from(int value) {
        return new Age(value);
    }

    public int value() {
        return value;
    }

    private boolean positive(int value) {
        return value > 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Age age = (Age) o;
        return value == age.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
