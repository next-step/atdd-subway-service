package nextstep.subway.path.fare.extra;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;

public class LineExtraFareStrategy implements ExtraFareStrategy {

    @Override
    public Fare calculate(Path path) {
        Sections sections = path.getSections();
        if (sections.isEmpty()) {
            return Fare.ZERO;
        }
        return sections.getAllLineFare()
                .stream()
                .max(Fare::compare)
                .orElseThrow(RuntimeException::new);
    }
}
