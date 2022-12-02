package nextstep.subway.line.domain;

import nextstep.subway.ErrorMessage;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class TempDistance {

    private static int LOWER_LIMIT = 0;

    private int tempDistance;

    public TempDistance(int distance) {
        if(distance <= LOWER_LIMIT) {
            throw new RuntimeException(
                    ErrorMessage.INVALID_DISTANCE_VALUE.setLimitValueAndGetMessage(String.valueOf(LOWER_LIMIT)));
        }
        this.tempDistance = distance;
    }

    protected TempDistance() {

    }

    public int getTempDistance() {
        return tempDistance;
    }

    public TempDistance subtract(TempDistance distance) {
        int newDistance = this.tempDistance - distance.getTempDistance();
        if (newDistance <= LOWER_LIMIT) {
            throw new RuntimeException(ErrorMessage.EXCEED_SECTION_DISTANCE.getMessage());
        }
        this.tempDistance = newDistance;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TempDistance that = (TempDistance) o;
        return tempDistance == that.tempDistance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tempDistance);
    }
}
