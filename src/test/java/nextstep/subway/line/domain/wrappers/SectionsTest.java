package nextstep.subway.line.domain.wrappers;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("구간 리스트 일급 컬렉션 객체 테스트")
class SectionsTest {
    private Line 신분당선;
    private Station 강남역;
    private Station 광교역;
    private Station 교대역;
    private Section 구간;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        강남역 = new Station(1L, "강남역");
        광교역 = new Station(2L, "광교역");
        교대역 = new Station(3L, "교대역");
        구간 = new Section(신분당선, 강남역, 광교역, 10);
    }

    @Test
    void 구간_정보_추가_up() {
        Section 교대역_광교역_구간 = new Section(신분당선, 교대역, 광교역, 10);
        Section 강남역_교대역_구간 = new Section(신분당선, 강남역, 교대역, 5);

        Sections sections = new Sections(new ArrayList<>(Arrays.asList(교대역_광교역_구간)));
        sections.updateSections(강남역_교대역_구간);
        assertThat(sections.stations()).containsExactly(강남역, 교대역, 광교역);
    }

    @Test
    void 구간_정보_추가_middle() {
        Sections sections = new Sections(new ArrayList<>(Arrays.asList(구간)));
        Section section = new Section(신분당선, 강남역, 교대역, 5);
        sections.updateSections(section);
        assertThat(sections.stations()).containsExactly(강남역, 교대역, 광교역);
    }

    @Test
    void 구간_정보_추가_down() {
        Section 강남역_교대역_구간 = new Section(신분당선, 강남역, 교대역, 5);
        Section 교대역_광교역_구간 = new Section(신분당선, 교대역, 광교역, 10);
        Sections sections = new Sections(new ArrayList<>(Arrays.asList(강남역_교대역_구간)));
        sections.updateSections(교대역_광교역_구간);
        assertThat(sections.stations()).containsExactly(강남역, 교대역, 광교역);
    }

    @Test
    void 구간_리스트_를_이용하여_지하철역_리스트_반환() {
        Sections sections = new Sections(Arrays.asList(구간, new Section(신분당선, 교대역, 강남역, 10)));
        List<Station> stations = sections.stations();
        assertThat(stations).containsExactly(교대역, 강남역, 광교역);
    }

    @Test
    void 상행_하행_모두_포함되지않는_지하철역_구간_등록_요청_시_에러_발생() {
        Sections sections = new Sections(Arrays.asList(구간));
        assertThatThrownBy(() -> sections.updateSections(new Section(신분당선, 교대역, new Station(7L, "서울대입구역"), 10))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 구간_정보_추가시_이미_등록된_구간일_경우_에러_발생() {
        Sections sections = new Sections(Arrays.asList(구간));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> sections.updateSections(구간)).withMessage("강남역, 광교역 구간은 이미 등록된 구간 입니다.");
    }
}
