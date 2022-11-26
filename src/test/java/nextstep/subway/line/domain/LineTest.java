package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    private Station 종각역;
    private Station 서울역;
    private Line line;

    @BeforeEach
    void setUp() {
        종각역 = new Station("종각역");
        서울역 = new Station("서울역");
        line = new Line("1호선", "blue", 종각역, 서울역, 10);
    }

    @DisplayName("노선에 포함된 지하철역 리스트 조회 작업을 성공한다")
    @Test
    void getStations() {
        assertThat(line.getStations()).containsExactly(종각역, 서울역);
    }

    @DisplayName("노선에 구간 추가 작업을 성공한다")
    @Test
    void addSection() {
        Station 시청역 = new Station("시청역");
        line.addSection(new Section(종각역, 시청역, 5));
        assertThat(line.getStations()).containsExactly(종각역, 시청역, 서울역);
    }
}
