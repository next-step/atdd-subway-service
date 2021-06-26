package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {

    private Sections sections;
    private Line 이호선;
    private Station 강남역;
    private Station 교대역;
    private Section section;

    @BeforeEach
    void setUp() {
        sections = new Sections();
        이호선 = new Line("2호선", "green");
        강남역 = new Station("강남역");
        교대역 = new Station("교대역");
        section = new Section(이호선, 강남역, 교대역, 10);
        sections.add(section);
    }

    @Test
    @DisplayName("구간 추가")
    void addSectionTest() {

        // then
        assertThat(sections.getSections()).hasSize(1);
    }

    @Test
    @DisplayName("구간 내에 있는 역 조회")
    void getStationsTest() {
        // given
        Station 서초역 = new Station("서초역");
        sections.add(new Section(이호선, 교대역, 서초역, 5));

        // then
        assertThat(sections.getStations()).hasSize(3);
    }

    @Test
    @DisplayName("이미 등록된 구간 등록 시 예외 발생")
    void addExistSectionTest() {
         assertThatThrownBy(() -> sections.add(section))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("연결할 수 없는 구간 등록 시 예외 발생")
    void addNotMatchedSectionTest() {
        // given
        Station 서초역 = new Station("서초역");
        Station 방배역 = new Station("방배역");

        // then
        assertThatThrownBy(() -> sections.add(new Section(이호선, 서초역, 방배역, 5)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("구간 사이 거리 이상의 구간 등록 시 예외 발생")
    void addNotValidDistanceSectionTest() {
        // given
        Station 서초역 = new Station("서초역");

        // then
        assertThatThrownBy(() -> sections.add(new Section(이호선, 강남역, 서초역, 10)))
                .isInstanceOf(RuntimeException.class);
    }
}