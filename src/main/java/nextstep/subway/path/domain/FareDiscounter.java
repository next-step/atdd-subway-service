package nextstep.subway.path.domain;

import org.springframework.stereotype.Component;

@Component
public class FareDiscounter {
    public int discountFare(int fare, int age) {
        if (isAdult(age)) {
            return fare;
        }
        if (isChild(age)) {
            return (int) ((fare - 350) * 0.5);
        }
        return (int) ((fare - 350) * 0.8);
    }

    public boolean isAdult(int age) {
        return age > 19;
    }

    public boolean isChild(int age) {
        return age < 14;
    }
}
