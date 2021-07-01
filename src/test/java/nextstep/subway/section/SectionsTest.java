package nextstep.subway.section;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {

    private Line 사호선;

    private Sections sections;
    private Section 서울_명동;

    @BeforeEach
    void setUp() {
        사호선 = new Line("사호선", "파란색");


        sections = new Sections();
        서울_명동 = new Section(사호선, 서울역, 명동역, 20);
        sections.add(서울_명동);
    }


    @DisplayName("구간 추가 통과케이스 : 신규 구간의 상행역이 존재")
    @Test
    void 구간추가_통과_상행역_이미존재() {
        //given
        Section 명동_충무로 = new Section(사호선, 명동역, 충무로역, 30);

        //when
        sections.add(명동_충무로);

        //then
        assertThat(sections.getSortedStation()).hasSize(3)
                .containsExactly(서울역, 명동역, 충무로역);
    }

    @DisplayName("구간 추가 통과케이스 : 신규 구간의 하행역이 존재")
    @Test
    void 구간추가_통과_하행역_이미존재() {
        //given
        Section 서울_회현 = new Section(사호선, 서울역, 회현역, 10);

        //when
        sections.add(서울_회현);

        //then
        assertThat(sections.getSortedStation()).hasSize(3)
                .containsExactly(서울역, 회현역, 명동역);
    }

    @DisplayName("구간 추가 예외케이스 : 기존 구간 중간에 추가되는데, 거리가 기존구간과 같거나 긺")
    @Test
    void 구간추가_예외_거리_같거나_긺() {
        //given
        Section 서울_회현 = new Section(사호선, 서울역, 회현역, 20);

        //when+then
        assertThatThrownBy(() -> sections.add(서울_회현)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("구간 추가 예외케이스 : 상하행역 이미 모두 존재")
    @Test
    void 구간추가_예외_상하행역_모두_이미_있음() {
        //when+then
        assertThatThrownBy(() -> sections.add(서울_명동)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("구간 추가 예외케이스 : 상하행역 모두 없음")
    @Test
    void 구간추가_예외_상하행역_모두_없음() {

        //Given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");

        Section 강남_양재 = new Section(사호선, 강남역, 양재역, 50);

        //when+then
        assertThatThrownBy(() -> sections.add(강남_양재)).isInstanceOf(RuntimeException.class);
    }
}
