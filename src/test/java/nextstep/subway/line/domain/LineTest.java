package nextstep.subway.line.domain;

import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    Line 신분당선;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "bg-red-600", new Station("강남역"), new Station("광교역"), 10);;
    }


    @Test
    @DisplayName("해당 노선의 구간의 역들을 조회한다")
    void getStations() {
        신분당선.getStations()

    }
}
