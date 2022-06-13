package nextstep.subway.utils;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class DomainCreationHelper {
    public static Sections 빈_섹션_생성() {
        return new Sections();
    }

    public static Station 역_생성(final String name) {
        return new Station(name);
    }

    public static Line 섹션_없는_라인_생성(final String name, final String color) {
        return new Line(name, color);
    }

    public static Distance 거리_생성(final int distance) {
        return new Distance(distance);
    }
}
