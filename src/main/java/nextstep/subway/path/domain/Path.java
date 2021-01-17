package nextstep.subway.path.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.path.domain.fare.AgeDiscount;
import nextstep.subway.station.domain.Station;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Path {
    private final List<Station> stations;
    private final int distance;
    private final int fare;

    public Path discountByAge(Integer age) {
        AgeDiscount ageDiscount = AgeDiscount.findAgeDiscount(age);
        int newFare = ageDiscount.discountFare(fare);
        return new Path(stations, distance, newFare);
    }
}
