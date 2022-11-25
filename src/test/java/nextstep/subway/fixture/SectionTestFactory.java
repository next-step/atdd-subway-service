package nextstep.subway.fixture;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SectionTestFactory {
    public static Section create(Station upStation, Station downStation, int distance) {
        return new Section(upStation, downStation, new Distance(distance));
    }

    private SectionTestFactory() {}
}
