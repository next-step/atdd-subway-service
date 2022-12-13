package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 구간 리스트 테스트")
class SectionsTest {

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
    @DisplayName("지하철 구간에 상행역을 등록할 수 있다.")
    void registration_of_subway_up_station() {
        // given
        Line 지하철_2호선 = new Line("2호선", "green", 강남역, 잠실역, 10);
        Section 신림_강남_구간 = new Section(신림역, 강남역, 10);

        // when
        지하철_2호선.addSection(신림_강남_구간);

        // then
        assertAll(
            () -> assertThat(지하철_2호선.getSections()).hasSize(2),
            () -> assertThat(지하철_2호선.stations()).contains(신림역, 강남역, 잠실역)
        );
    }

    @Test
    @DisplayName("지하철 구간에 하행역을 등록할 수 있다.")
    void registration_of_subway_down_station() {
        // given
        Line 지하철_2호선 = new Line("2호선", "green", 강남역, 잠실역, 10);
        Section 잠심_왕십리_구간 = new Section(잠실역, 삼성역, 10);

        // when
        지하철_2호선.addSection(잠심_왕십리_구간);

        // then
        assertAll(
            () -> assertThat(지하철_2호선.getSections()).hasSize(2),
            () -> assertThat(지하철_2호선.stations()).contains(강남역, 잠실역, 삼성역)
        );
    }

    @Test
    @DisplayName("지하철 첫 구간 삭제 할 수 있다.")
    void delete_first_section() {
        // given
        Sections sections = new Sections();
        Section 신림_강남_구간 = new Section(신림역, 강남역, 10);
        sections.addSection(신림_강남_구간);
        Section 강남_잠실_구간 = new Section(강남역, 잠실역, 10);
        sections.addSection(강남_잠실_구간);

        // when
        sections.deleteSection(신림역);

        // then
        assertThat(sections.stationsBySorted())
            .containsExactly(강남역, 잠실역);
    }



    @Test
    @DisplayName("지하철 구간이 하나인 경우 삭제 불가")
    void delete_one_section() {
        // given
        Sections sections = new Sections();
        sections.addSection(new Section(신림역, 강남역, 10));

        // when && then
        assertThatThrownBy(() -> sections.deleteSection(신림역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("지하철 구간이 1개인 경우 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("지하철에 없는 구간은 삭제 불가")
    void delete_not_exists_section() {
        // given
        Sections sections = new Sections();
        sections.addSection(new Section(신림역, 강남역, 10));
        sections.addSection(new Section(강남역, 잠실역, 10));

        // given && then
        assertThatThrownBy(() -> sections.deleteSection(삼성역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("삭제하려는 지하철 역이 올바르지 않습니다.");
    }

}