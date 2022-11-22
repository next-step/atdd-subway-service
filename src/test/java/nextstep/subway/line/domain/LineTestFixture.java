package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

public class LineTestFixture {

    public static Line createLine(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }
}
