package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private Line 이호선;
    private Station 잠실역;
    private Station 강남역;
    private Station 역삼역;
    private Sections sections = Sections.newInstance();

    @BeforeEach
    void before() {
        잠실역 = new Station("잠실역");
        역삼역 = new Station("역삼역");
        강남역 = new Station("강남역");

        // given : 이호선에 구간 등록 잠실역 -10- 역삼역 -5- 강남역
        이호선 = new Line("2호선", "green", 잠실역, 강남역,15, 500);
        sections.add(new Section(잠실역, 역삼역, 10));
        sections.add(new Section(역삼역, 강남역, 5));
    }

    @Test
    @DisplayName("모든 구간의 지하철역을 조회한다.")
    void getStations() {
        //when : 이호선에 등록된 모든 역을 조회
        List<Station> stations = sections.getStations();

        //then : 지하철 역이 조회됨
        assertThat(stations).containsExactly(잠실역, 역삼역, 강남역);
    }

    @Test
    @DisplayName("구간을 추가한다.")
    void addSectionTest() {
        // 잠실역 -2- 선릉역 -8- 역삼역 -5- 강남역
        //when : 이호선에 잠실역 선릉역 구간 추가
        Station 선릉역 = new Station("선릉역");
        Section newSection = new Section(잠실역, 선릉역, 2);
        sections.add(newSection);

        //then : 이호선에 잠실역-선릉역 구간이 추가됨
        assertThat(sections.getStations()).containsExactly(잠실역, 선릉역, 역삼역, 강남역);
    }

    @Test
    @DisplayName("지하철역을 삭제한다.")
    void removeStationTest() {
        //삭제전: 잠실역 -5- 신천역 -3- 선릉역 -2- 역삼역 -5- 강남역
        //삭제후: 잠실역 -5- 신천역 -5- 역삼역 -5- 강남역

        //given : 이호선에 잠실역-신천역, 신천역-선릉역 구간 추가
        Station 선릉역 = new Station("선릉역");
        Station 신천역 = new Station("신천역");
        Section newSection = new Section(잠실역, 신천역, 5);
        sections.add(newSection);
        Section newSection2 = new Section(신천역, 선릉역, 3);
        sections.add(newSection2);

        //when : 이호선에서 선릉역 삭제
        sections.delete(선릉역, 이호선);

        //then : 선릉역이 삭제됨
        assertThat(sections.getStations()).containsExactly(잠실역, 신천역, 역삼역, 강남역);
    }

    @Test
    @DisplayName("역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음")
    void sectionTest1() {
        //when : 기존역 사이보다 긴 구간 추가
        Station 삼성역 = new Station("삼성역");
        Section 새로운_구간 = new Section(잠실역, 삼성역, 20);

        //then : 구간 추가 실패
        assertThatThrownBy(
                () -> sections.add(새로운_구간)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음")
    void sectionTest2() {
        //when : 기존에 존재하는 구간 추가
        Section 새로운_구간 = new Section(강남역, 역삼역, 20);

        //then : 구간 추가 실패
        assertThatThrownBy(
                () -> sections.add(새로운_구간)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음")
    void sectionTest3() {
        //when : 기존 노선에 상행역과 하행역 둘 중 하나도 포함되어있지 않은 구간 추가
        Station 토쿄역 = new Station("토쿄역");
        Station 간사이역 = new Station("간사이역");
        Section 새로운_구간 = new Section(토쿄역, 간사이역, 20);

        //then : 구간 추가 실패
        assertThatThrownBy(
                () -> sections.add(새로운_구간)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("노선에 없는 역을 삭제 하는 경우")
    void sectionTest4() {
        //when : 기존 노선에 존재 하지 않은 구간 삭제
        Station 토쿄역 = new Station("토쿄역");

        //then : 구간 삭제 실패
        assertThatThrownBy(
                () -> sections.delete(토쿄역, 이호선)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("구간이 하나인 노선에서 지하철역을 삭제 하는 경우")
    void sectionTest5() {
        //when : 구간이 하나인 노선에 지하철역 삭제
        Station 가락시장역 = new Station("가락시장역");
        Station 경찰병원역 = new Station("경찰병원역");

        Sections line3_sections = Sections.from(
                Arrays.asList(new Section(가락시장역, 경찰병원역, 5))
        );

        //then : 구간 추가 실패
        assertThatThrownBy(
                () -> line3_sections.delete(가락시장역, 이호선)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
