package nextstep.subway.path.domain.overfare;

import org.springframework.stereotype.Component;

@Component
public class DefaultOverFare implements OverFare {

    private final int DEFAULT_FARE = 1250;
    private final int DEFAULT_DISTANCE = 10;
    private OverFare overFare;

    public DefaultOverFare() {
        this.overFare = new LowOverFare();
    }

    @Override
    public int calculate(int distance) {
        if(distance <= DEFAULT_DISTANCE) {
            return DEFAULT_FARE;
        }
        return DEFAULT_FARE + overFare.calculate(distance - DEFAULT_DISTANCE);
    }
}
