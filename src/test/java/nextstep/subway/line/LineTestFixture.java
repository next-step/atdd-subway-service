package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class LineTestFixture {
    public static Line 노선을_생성한다(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public static Line 노선을_생성하고_하나의_역을_등록한다(String name, String color, Station upStation, Station downStation, int distance) {
        Line line = 노선을_생성한다(name, color, upStation, downStation, distance);
        line.addStation(upStation, new Station("부천역"), 5);
        return line;
    }
}
