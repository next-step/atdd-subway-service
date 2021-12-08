package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @Test
    @DisplayName("노선 정보를 수정한다.")
    public void update() throws Exception {
        // given
        Line line = new Line("4호선", "skyblue");
        Line updateLine = new Line("1호선", "blue");

        // when
        line.update(updateLine);

        // then
        assertThat(line.getName()).isEqualTo(updateLine.getName());
        assertThat(line.getColor()).isEqualTo(updateLine.getColor());
    }

    @Test
    @DisplayName("Line 객체를 생성한다.")
    public void of() throws Exception {
        // when
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Line result = Line.of("신분당선", "red", 강남역, 양재역, 10);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getStations()).containsExactly(강남역, 양재역);
    }
}