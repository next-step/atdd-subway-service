package nextstep.subway.line.domain;

import nextstep.subway.line.consts.ErrorMessage;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    public static final int MINIMUM_DISTANCE = 1;

    private int distance;

    protected Distance() {
    }

    private Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    private void validateDistance(int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new IllegalArgumentException(
                    String.format(ErrorMessage.ERROR_DISTANCE_TOO_SHORT, MINIMUM_DISTANCE, distance)
            );
        }
    }

    public void add(Distance distance) {
        validateDistance(this.distance + distance.getDistance());
        this.distance += distance.getDistance();
    }

    public void subtract(Distance distance) {
        validateDistance(this.distance - distance.getDistance());
        this.distance -= distance.getDistance();
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Distance)) {
            return false;
        }
        return ((Distance)obj).getDistance() == distance;
    }
}
