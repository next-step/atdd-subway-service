package nextstep.subway.line.domain;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.ErrorMessage;

@Embeddable
public class Distance {

    private static int MINIMUM_DISTANCE = 0;
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
            throw new IllegalArgumentException(ErrorMessage.notValidDistance(MINIMUM_DISTANCE));
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
