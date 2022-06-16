package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.station.domain.StationTest.지하철_생성;
import static org.assertj.core.api.Assertions.*;

class SectionsTest {

    @DisplayName("구간 목록의 구간 리스트롤 조회 시 시작에서 종점역 순으로 정렬되어 역이 조회되어야 한다")
    @Test
    void getAllStationsInSections() {
        // given
        Station 동대문역사문화공원역 = 지하철_생성("동대문역사문화공원역");
        Station 뚝섬역 = 지하철_생성("뚝섬역");
        Station 상왕십리역 = 지하철_생성("상왕십리역");
        Station 한양대역 = 지하철_생성("한양대역");
        Station 신당역 = 지하철_생성("신당역");
        Station 왕십리역 = 지하철_생성("왕십리역");

        // when
        Sections 구간_목록 = 구간_목록_생성(동대문역사문화공원역, 왕십리역, 50);
        구간_목록에_구간_추가(구간_목록, 동대문역사문화공원역, 신당역, 10);
        구간_목록에_구간_추가(구간_목록, 왕십리역, 한양대역, 10);
        구간_목록에_구간_추가(구간_목록, 신당역, 상왕십리역, 15);
        구간_목록에_구간_추가(구간_목록, 한양대역, 뚝섬역, 15);

        // then
        assertThat(노선의_지하철_이름(구간_목록))
                .containsExactly(
                        동대문역사문화공원역.getName(), 신당역.getName(), 상왕십리역.getName(),
                        왕십리역.getName(), 한양대역.getName(), 뚝섬역.getName()
                );
    }

    @DisplayName("구간 목록에 상/하 구간을 추가하면 해당 위치에 정상 추가되어야 한다")
    @Test
    void sectionAddAtSideTest() {
        // given
        Station 동대문역사문화공원역 = 지하철_생성("동대문역사문화공원역");
        Station 을지로4가역 = 지하철_생성("을지로4가역");
        Station 을지로3가역 = 지하철_생성("을지로3가역");
        Sections 상행_추가될_구간 = 구간_목록_생성(을지로4가역, 을지로3가역, 10);
        Sections 하행_추가될_구간 = 구간_목록_생성(을지로3가역, 을지로4가역, 10);

        // when
        구간_목록에_구간_추가(상행_추가될_구간, 동대문역사문화공원역, 을지로4가역, 20);
        구간_목록에_구간_추가(하행_추가될_구간, 을지로4가역, 동대문역사문화공원역, 30);

        // then
        assertThat(노선의_지하철_이름(상행_추가될_구간))
                .containsExactly(동대문역사문화공원역.getName(), 을지로4가역.getName(), 을지로3가역.getName());
        assertThat(노선의_구간_거리(상행_추가될_구간))
                .containsExactly(20, 10);
        assertThat(노선의_지하철_이름(하행_추가될_구간))
                .containsExactly(을지로3가역.getName(), 을지로4가역.getName(), 동대문역사문화공원역.getName());
        assertThat(노선의_구간_거리(하행_추가될_구간))
                .containsExactly(10, 30);
    }

    @DisplayName("구간 중간에 구간을 추가하면 해당 위치에 정상 추가되고 이전 구간은 추가된 구간만큼 거리가 줄어들어야 한다")
    @Test
    void sectionAddAtMiddleTest() {
        // given
        Station 동대문역사문화공원역 = 지하철_생성("동대문역사문화공원역");
        Station 을지로3가역 = 지하철_생성("을지로3가역");
        Station 을지로4가역 = 지하철_생성("을지로4가역");
        Sections 구간_목록 = 구간_목록_생성(동대문역사문화공원역, 을지로3가역, 10);

        // when
        구간_목록에_구간_추가(구간_목록, 동대문역사문화공원역, 을지로4가역, 4);

        // then
        assertThat(노선의_지하철_이름(구간_목록))
                .containsExactly(동대문역사문화공원역.getName(), 을지로4가역.getName(), 을지로3가역.getName());
        assertThat(노선의_구간_거리(구간_목록))
                .containsExactly(4, 6);
    }

    @DisplayName("추가하려는 구간의 길이보다 큰 구간을 추가하면 예외가 발생해야 한다")
    @Test
    void sectionAddBiggerDistanceTest() {
        // given
        Station 동대문역사문화공원역 = 지하철_생성("동대문역사문화공원역");
        Station 을지로3가역 = 지하철_생성("을지로3가역");
        Station 을지로4가역 = 지하철_생성("을지로4가역");
        Sections 구간_목록 = 구간_목록_생성(동대문역사문화공원역, 을지로3가역, 10);

        // then
        assertThatThrownBy(() -> 구간_목록에_구간_추가(구간_목록, 동대문역사문화공원역, 을지로4가역, 11))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("구간에 존재하지 않는 역으로만 구간을 추가하면 예외가 발생해야 한다")
    @Test
    void sectionAddNotContainStationsTest() {
        // given
        Station 왕십리역 = 지하철_생성("왕십리역");
        Station 한양대역 = 지하철_생성("한양대역");
        Station 신촌역 = 지하철_생성("신촌역");
        Station 홍대입구역 = 지하철_생성("홍대입구역");
        Sections 구간_목록 = 구간_목록_생성(왕십리역, 한양대역, 10);

        // then
        assertThatThrownBy(() -> 구간_목록에_구간_추가(구간_목록, 신촌역, 홍대입구역, 5))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("2개 이상의 구간을 가진 구간 목록의 시작/종점 구간을 삭제하면 해당 구간이 삭제되고 붙어있는 구간이 시작/종점 구간이 되어야 한다")
    @Test
    void removeSectionsSideTest() {
        // given
        Station 왕십리역 = 지하철_생성("왕십리역");
        Station 한양대역 = 지하철_생성("한양대역");
        Station 뚝섬역 = 지하철_생성("뚝섬역");
        Sections 시작_구간이_삭제될_구간_목록 = 구간_목록_생성(왕십리역, 한양대역, 10);
        Sections 종점_구간이_삭제될_구간_목록 = 구간_목록_생성(왕십리역, 한양대역, 10);
        구간_목록에_구간_추가(시작_구간이_삭제될_구간_목록, 한양대역, 뚝섬역, 10);
        구간_목록에_구간_추가(종점_구간이_삭제될_구간_목록, 한양대역, 뚝섬역, 10);

        // when
        구간_목록의_특정_구간_삭제(시작_구간이_삭제될_구간_목록,  왕십리역);
        구간_목록의_특정_구간_삭제(종점_구간이_삭제될_구간_목록, 뚝섬역);

        // then
        assertThat(노선의_지하철_이름(시작_구간이_삭제될_구간_목록))
                .containsExactly(한양대역.getName(), 뚝섬역.getName());
        assertThat(노선의_지하철_이름(종점_구간이_삭제될_구간_목록))
                .containsExactly(왕십리역.getName(), 한양대역.getName());
    }

    @DisplayName("3개 이상의 구간을 가진 구간 목록의 중간 구간을 삭제하면 해당 구간은 삭제되고 앞 구간이 삭제된 구간의 길이만큼 증가되어야 한다")
    @Test
    void removeSectionMiddleTest() {
        // given
        Station 왕십리역 = 지하철_생성("왕십리역");
        Station 한양대역 = 지하철_생성("한양대역");
        Station 뚝섬역 = 지하철_생성("뚝섬역");
        Station 성수역 = 지하철_생성("성수역");
        Sections 구간_목록 = 구간_목록_생성(왕십리역, 한양대역, 3);
        구간_목록에_구간_추가(구간_목록, 한양대역, 뚝섬역, 4);
        구간_목록에_구간_추가(구간_목록, 뚝섬역, 성수역, 5);

        // when
        구간_목록의_특정_구간_삭제(구간_목록, 한양대역);

        // then
        assertThat(노선의_지하철_이름(구간_목록))
                .containsExactly(왕십리역.getName(), 뚝섬역.getName(), 성수역.getName());
        assertThat(노선의_구간_거리(구간_목록))
                .containsExactly(7, 5);
    }

    public static Sections 구간_목록_생성(Station upStation, Station downStation, int distance) {
        Sections result = new Sections();
        구간_목록에_구간_추가(result, upStation, downStation, distance);

        return result;
    }

    private static void 구간_목록에_구간_추가(Sections sections, Station upStation, Station downStation, int distance) {
        Section section = new Section(new Line(), upStation, downStation, new Distance(distance));
        sections.addSection(section);
    }

    private void 구간_목록의_특정_구간_삭제(Sections sections, Station station) {
        sections.removeStation(new Line(), station);
    }

    private List<String> 노선의_지하철_이름(Sections sections) {
        return sections.getStations()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }

    private List<Integer> 노선의_구간_거리(Sections sections) {
        List<String> lineNames = 노선의_지하철_이름(sections);

        return lineNames.subList(0, lineNames.size() - 1).stream()
                .map(name -> 지하철_이름이_상행역인_구간_찾기(sections, name).getDistance().getValue())
                .collect(Collectors.toList());
    }

    private Section 지하철_이름이_상행역인_구간_찾기(Sections sections, String name) {
        return sections.getValue()
                .stream()
                .filter(it -> it.getUpStation().getName().equals(name))
                .findFirst()
                .orElseGet(() -> fail("구간을 찾을 수 없습니다."));
    }
}
