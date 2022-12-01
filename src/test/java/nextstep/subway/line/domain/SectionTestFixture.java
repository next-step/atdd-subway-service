package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

public class SectionTestFixture {
    public static Section section(Station upStation, Station downStation, int distance) {
        return Section.of(upStation, downStation, Distance.from(distance));
    }
}
