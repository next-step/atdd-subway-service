package nextstep.subway.section;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

public class SectionTest {

    private Line 사호선;
    Station 회현역;
    Station 서울역;
    Station 명동역;

    @BeforeEach
    void setUp(){
        사호선 = new Line("사호선", "파란색");

        회현역 = new Station("회현역");
        서울역 = new Station("서울역");
        명동역 = new Station("명동역");
    }
}
