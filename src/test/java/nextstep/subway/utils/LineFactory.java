package nextstep.subway.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Line.Builder;
import nextstep.subway.station.domain.Station;

public class LineFactory {
    public static Line create(String name, String color, Station upStation, Station downStation, int distance){
        return new Builder(name,color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }
}
