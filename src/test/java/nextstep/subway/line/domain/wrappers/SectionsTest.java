package nextstep.subway.line.domain.wrappers;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 리스트 일급 컬렉션 객체 테스트")
class SectionsTest {
    private Line 신분당선;
    private Station 강남역;
    private Station 광교역;
    private Section 구간;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        구간 = new Section(신분당선, 강남역, 광교역, 10);
    }

    @Test
    void 구간_정보_추가() {
        Sections sections = new Sections();
        sections.addSection(구간);
        assertThat(sections).isEqualTo(new Sections(Arrays.asList(구간)));
    }

    @Test
    void 구간_리스트_를_이용하여_지하철역_리스트_반환() {
        Sections sections = new Sections(Arrays.asList(구간));
        List<Station> stations = sections.stations();
        assertThat(stations).containsExactly(강남역, 광교역);
    }
}