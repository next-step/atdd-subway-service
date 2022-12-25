package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("지하철 라인 테스트")
class LineTest {

    private Station 신림역;
    private Station 강남역;
    private Station 잠실역;
    private Station 삼성역;
    private Line 지하철_2호선;
    private Line 지하철_3호선;

    @BeforeEach
    void init() {
        신림역 = new Station("신림역");
        강남역 = new Station("강남역");
        잠실역 = new Station("잠실역");
        삼성역 = new Station("삼성역");
        지하철_2호선 = new Line("2호선", "green", 신림역, 강남역, 20, 1000);
    }

    @Test
    @DisplayName("지하철 중간 구간을 삭제 할 수 있다.")
    void line_mid_delete() {
        // given
        지하철_2호선.addSection(강남역, 잠실역, 10);

        // when
        지하철_2호선.removeSection(강남역);

        // then
        assertAll(
            () -> assertThat(지하철_2호선.getSections()).hasSize(1),
            () -> assertThat(지하철_2호선.getStations()).contains(신림역, 잠실역)
        );
    }

    @Test
    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없다.")
    void up_down_station_enroll() {
        // given
        assertThatThrownBy(
            () -> 지하철_2호선.addSection(신림역, 강남역, 20)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("상행역과 하행역이 둘 중 하나도 포함되어 있지 않으면 구간을 추가할 수 없다.")
    void no_exist_up_down_station() {
        // given
        // when
        Station newStation1 = new Station("시청역");
        Station newStation2 = new Station("왕십리역");

        // then
        assertThatThrownBy(
            () -> 지하철_2호선.addSection(newStation1, newStation2, 30)
        ).isInstanceOf(RuntimeException.class);
    }


}
