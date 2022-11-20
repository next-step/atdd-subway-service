package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

public class SectionTestFixture {

    public static Section createSection(Line line, Station upStation, Station downStation, int distance) {
        return new Section(line, upStation, downStation, distance);
    }
}
