package nextstep.subway.path.domain;

import java.util.Objects;

public class Fee {
    private int fee;

    public Fee() {
    }

    public int getFee() {
        return fee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fee fee1 = (Fee) o;
        return fee == fee1.fee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fee);
    }
}
