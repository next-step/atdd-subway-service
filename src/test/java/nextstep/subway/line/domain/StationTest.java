package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Station 클래스")
public class StationTest {
    public static Station 역_생성(final String name) {
        return new Station(name);
    }
}
