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

    @BeforeEach
    void init() {
        신림역 = new Station("신림역");
        강남역 = new Station("강남역");
        잠실역 = new Station("잠실역");
        삼성역 = new Station("삼성역");
    }

    @Test
    @DisplayName("지하철 중간 구간을 삭제 할 수 있다.")
    void line_mid_delete() {
        // given
        Line 지하철_2호선 = new Line("2호선", "green", 신림역, 강남역, 10);
        Section 강남_잠실_구간 = new Section(강남역, 잠실역, 10);
        지하철_2호선.addSection(강남_잠실_구간);

        // when
        지하철_2호선.deleteSection(강남역);

        // then
        assertAll(
            () -> assertThat(지하철_2호선.getSections()).hasSize(1),
            () -> assertThat(지하철_2호선.stations()).contains(신림역, 잠실역)
        );
    }

    @Test
    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없다.")
    void up_down_station_enroll() {
        // given
        Line 지하철_2호선 = new Line("2호선", "green", 신림역, 강남역, 10);
        Section 강남_잠실_구간 = new Section(강남역, 잠실역, 10);
        지하철_2호선.addSection(강남_잠실_구간);

        Section 신림_잠실_구간 = new Section(신림역, 잠실역, 10);
        // when && then
        assertThatThrownBy(() -> 지하철_2호선.addSection(신림_잠실_구간))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 등록된 구간 입니다.");
    }

    @Test
    @DisplayName("상행역과 하행역이 둘 중 하나도 포함되어 있지 않으면 구간을 추가할 수 없다.")
    void no_exist_up_down_station() {
        // given
        Line 지하철_2호선 = new Line("2호선", "green", 신림역, 강남역, 10);
        Section 잠실_왕십리_구간 = new Section(잠실역, 삼성역, 10);

        // when && then
        assertThatThrownBy(() -> 지하철_2호선.addSection(잠실_왕십리_구간))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("등록할 수 없는 구간 입니다.");
    }

    @Test
    @DisplayName("지하철노선 길이보다 큰 구역은 추가할 수 없다.")
    void add_impossible_over_distance_section() {
        // given
        Line 지하철_2호선 = new Line("2호선", "green", 신림역, 잠실역, 10);
        Section 신림_강남_구간 = new Section(신림역, 강남역, 10);

        // when && then
        assertThatThrownBy(() -> 지하철_2호선.addSection(신림_강남_구간))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

}
