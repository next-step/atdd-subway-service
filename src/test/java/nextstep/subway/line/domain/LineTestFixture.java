package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

public class LineTestFixture {
    public static Line createLine(String name, Station upStation, Station downStation, int distance) {
        return Line.builder()
                .name(name)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }
}
