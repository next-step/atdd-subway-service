package nextstep.subway.line.domain;


import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static int MINIMUM_DISTANCE = 0;
    private static String DISTANCE_LOWER_THEN_MINIMUM = String.format("거리는 %d보다 커야 합니다.", MINIMUM_DISTANCE);

    @Column(name = "distance", nullable = false)
    private int value;

    protected Distance() {}

    private Distance(final int distance){
        this.value = distance;
    }

    public static Distance from(final int distance) {
        validateDistance(distance);
        return new Distance(distance);
    }

    private static void validateDistance(int distance) {
        if(distance<=MINIMUM_DISTANCE){
            throw new IllegalArgumentException(DISTANCE_LOWER_THEN_MINIMUM);
        }
    }


    public int value() {
        return value;
    }

    public Distance add(Distance distance) {
        return Distance.from(this.value + distance.value());
    }


    public Distance subtract(Distance distance) {
        return Distance.from(this.value - distance.value());
    }
}
