package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LinesTest {

    private Station 강남역;
    private Station 잠실역 ;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        잠실역 = new Station("잠실역");
        이호선 = new Line("2호선", "green", 강남역, 잠실역, 20);
        ReflectionTestUtils.setField(강남역, "id", 1L);
        ReflectionTestUtils.setField(잠실역, "id", 2L);
    }

    @DisplayName("노선목록(List<Line>)에서 id 값으로 지하철 역을 찾는다.")
    @Test
    void findStationById() {
        List<Line> inputLines = Arrays.asList(이호선);
        Lines lines = new Lines(inputLines);

        Station findedStation = lines.findStationById(잠실역.getId());

        assertThat(findedStation).isSameAs(잠실역);
    }
}
