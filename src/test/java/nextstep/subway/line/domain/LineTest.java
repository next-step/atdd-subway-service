package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LineTest {
    private static final Station 강남역 = Station.of(1L, "강남역");
    private static final Station 양재역 = Station.of(2L, "양재역");
    private static final Station 청계산입구역 = Station.of(3L, "청계산입구역");
    private static final Station 판교역 = Station.of(4L, "판교역");

    @Test
    void update() {
        // given
        Line standard = new Line("신분당선", "bg-red-600");
        Line expected = new Line("구신분당선", "bg-red-700");

        // when
        standard.update(expected);

        // then
        assertAll(
                () -> assertThat(standard.getName()).isEqualTo(expected.getName()),
                () -> assertThat(standard.getColor()).isEqualTo(expected.getColor())
        );
    }

    @DisplayName("노선에 새로운 구간을 등록한다.")
    @Test
    void addSection() {
        // given
        Line line = new Line("신분당선", "bg-red-600", 강남역, 양재역, 2);

        // when
        line.addSection(양재역, 청계산입구역, 2);

        // then
        assertThat(line.getSections()).hasSize(2);
    }

    @DisplayName("상행역 -> 하행역으로 정렬된 역목록을 반환한다.")
    @Test
    void getSortedStations() {
        // given
        Line line = new Line("신분당선", "bg-red-600", 양재역, 판교역, 4);

        // when
        line.addSection(강남역, 양재역, 2);

        // then
        assertThat(line.getSortedStations()).containsExactly(강남역, 양재역, 판교역);
    }
}
