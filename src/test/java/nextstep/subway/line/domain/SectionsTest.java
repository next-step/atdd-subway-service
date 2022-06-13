package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import nextstep.subway.exception.domain.SubwayException;
import nextstep.subway.exception.domain.SubwayExceptionMessage;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.ListTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    Station 강남역;
    Station 판교역;
    Station 광교역;
    Line 신분당선;
    Section 강남역_판교역;
    Section 판교역_광교역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
        광교역 = new Station("광교역");
        신분당선 = new Line("신분당선", "bg-red-600");
        강남역_판교역 = new Section(신분당선, 강남역, 판교역, 5);
        판교역_광교역 = new Section(신분당선, 판교역, 광교역, 8);
    }

    @Test
    @DisplayName("지하철 구간 콜렉션에서 순서대로 지하철역을 가져오는 정상 테스트")
    void getStations() {
        // given
        final Sections sections = new Sections(ListTestUtils.newList(강남역_판교역, 판교역_광교역));

        // when
        final List<Station> stations = sections.getStations();

        // then
        assertAll(
                () -> assertThat(stations).hasSize(3),
                () -> assertThat(stations).containsExactly(강남역, 판교역, 광교역)
        );
    }

    @Test
    @DisplayName("지하철 구간 콜렉션에 지하철 구간 추가 성공 테스트")
    void add() {
        // given
        final Sections sections = new Sections(ListTestUtils.newList(강남역_판교역, 판교역_광교역));

        final Station 동천역 = new Station("동천역");
        final Section section = new Section(신분당선, 동천역, 광교역, 4);

        // when
        sections.add(section);

        // then
        assertAll(
                () -> assertThat(sections.getList()).hasSize(3),
                () -> assertThat(sections.getStations()).hasSize(4),
                () -> assertThat(sections.getStations()).containsExactly(강남역, 판교역, 동천역 ,광교역)
        );
    }

    @Test
    @DisplayName("중복구간으로 지하철 구간 추가 실패 ")
    void add_exception_duplicate() {
        // given
        final Sections sections = new Sections(ListTestUtils.newList(강남역_판교역, 판교역_광교역));

        // when & then
        assertThatThrownBy(() -> sections.add(강남역_판교역))
                .isInstanceOf(SubwayException.class)
                .hasMessage(SubwayExceptionMessage.DUPLICATE_SECTION.getMessage());
    }

    @Test
    @DisplayName("연속되지 않은 구간으로 지하철 구간 추가 실패 ")
    void add_exception_not_continuous() {
        // given
        final Sections sections = new Sections(ListTestUtils.newList(강남역_판교역, 판교역_광교역));

        final Station 동천역 = new Station("동천역");
        final Station 미금역 = new Station("미금역");

        final Section section = new Section(신분당선, 미금역, 동천역, 1);

        // when & then
        assertThatThrownBy(() -> sections.add(section))
                .isInstanceOf(SubwayException.class)
                .hasMessage(SubwayExceptionMessage.NOT_REGISTER_SECTION.getMessage());
    }

    @Test
    @DisplayName("추가하는 구간이 기존 보다 큰 경우 지하철 구간 추가 실패 ")
    void add_exception_() {
        // given
        final Sections sections = new Sections(ListTestUtils.newList(강남역_판교역, 판교역_광교역));

        final Station 동천역 = new Station("동천역");

        final Section section = new Section(신분당선, 동천역, 광교역, 30);

        // when & then
        assertThatThrownBy(() -> sections.add(section))
                .isInstanceOf(SubwayException.class)
                .hasMessage(SubwayExceptionMessage.OVER_THE_DISTANCE.getMessage());
    }
}
