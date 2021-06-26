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
    private Station 서초역;
    private Section section;
    private Station 방배역;

    @BeforeEach
    void setUp() {
        sections = new Sections();
        이호선 = new Line("2호선", "green");
        강남역 = new Station("강남역");
        교대역 = new Station("교대역");
        서초역 = new Station("서초역");
        방배역 = new Station("방배역");
        section = new Section(이호선, 강남역, 교대역, 10);
        sections.add(section);
    }

    @Test
    @DisplayName("구간을 추가한다")
    void addSectionTest() {

        // then
        assertThat(sections.getSections()).hasSize(1);
    }

    @Test
    @DisplayName("구간 내에 있는 역을 조회한다")
    void getStationsTest() {
        // given
        sections.add(new Section(이호선, 교대역, 서초역, 5));

        // then
        assertThat(sections.getStations()).hasSize(3);
    }

    @Test
    @DisplayName("이미 등록된 구간 등록 시 예외가 발생한다")
    void addExistSectionTest() {
         assertThatThrownBy(() -> sections.add(section))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("연결할 수 없는 구간 등록 시 예외가 발생한다")
    void addNotMatchedSectionTest() {

        // then
        assertThatThrownBy(() -> sections.add(new Section(이호선, 서초역, 방배역, 5)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("구간 사이 거리 이상의 구간 등록 시 예외가 발생한다")
    void addNotValidDistanceSectionTest() {

        // then
        assertThatThrownBy(() -> sections.add(new Section(이호선, 강남역, 서초역, 10)))
                .isInstanceOf(RuntimeException.class);
    }
    
    @Test
    @DisplayName("구간 내의 역을 삭제한다")
    void removeStationTest() {
        // given
        sections.add(new Section(이호선, 교대역, 서초역, 5));

        // when
        sections.removeStation(이호선, 서초역);

        // then
        assertThat(sections.getStations()).hasSize(2);
    }
}