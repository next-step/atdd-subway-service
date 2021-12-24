package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class LineTestFixture {
    public final static Station upStationFirstLine = new Station("소사역");
    public final static Station downStationFirstLine = new Station("온수역");
    public final static Station addStationFirstLine = new Station("역곡역");
    public final static Station upStationSecondLine = new Station("온수역");
    public final static Station downStationSecondLine = new Station("대림역");
    public final static Station upStationThirdLine = new Station("대림역");
    public final static Station downStationThirdLine = new Station("소사역");

    public static Line 노선을_생성한다(String name, String color, Station upStation, Station downStation, int distance, int overFare) {
        return new Line(name, color, upStation, downStation, distance, overFare);
    }

    public static Line 노선을_생성하고_하나의_역을_등록한다(String name, String color, Station upStation, Station downStation, int distance, int overFare) {
        Line line = 노선을_생성한다(name, color, upStation, downStation, distance, overFare);
        line.addStation(upStation, new Station("부천역"), 5);
        return line;
    }
}
