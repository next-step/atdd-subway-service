package nextstep.subway.line.domain;

import nextstep.subway.enums.ErrorMessage;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        this.distance = distance;
    }


    public Distance subtract(Distance newDistance) {
        isValidDistance(newDistance);
        return new Distance(this.distance - newDistance.distance);
    }

    private void isValidDistance(Distance newDistance) {
        if (this.distance <= newDistance.distance) {
            throw new IllegalArgumentException(ErrorMessage.EXCEEDED_DISTANCE.getMessage());
        }
    }

    public int getDistance(){
        return this.distance;
    }



}
