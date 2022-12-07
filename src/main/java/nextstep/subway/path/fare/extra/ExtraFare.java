package nextstep.subway.path.fare.extra;

import java.util.Arrays;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;

public enum ExtraFare {
    BASIC(new BasicExtraFareStrategy()),
    UNTIL_FIFTY_KILO(new UntilFiftyKiloExtraFareStrategy()),
    ABOVE_FIFTY(new AboveFiftyKiloExtraFareStrategy()),
    LINE(new LineExtraFareStrategy())
    ;

    private final ExtraFareStrategy extraFareStrategy;

    ExtraFare(ExtraFareStrategy extraFareStrategy) {
        this.extraFareStrategy = extraFareStrategy;
    }

    public static Fare calculateExtraFare(Path path) {
        return Arrays.stream(ExtraFare.values())
                     .map(farePolicy -> farePolicy.calculate(path))
                     .reduce(Fare.ZERO, Fare::add);
    }

    public Fare calculate(Path path) {
        return extraFareStrategy.calculate(path);
    }

}
