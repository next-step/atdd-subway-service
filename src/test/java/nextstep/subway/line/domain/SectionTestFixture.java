package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

public class SectionTestFixture {

    public static Section createSection(Line line, Station upStation, Station downStation, int distance) {
        return Section.of(line, upStation, downStation, distance);
    }
}
