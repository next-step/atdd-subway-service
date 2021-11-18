package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 구간 목록 테스트")
class SectionsTest {

    private Station 강남역;
    private Station 역삼역;
    private Station 사당역;
    private Line 이호선;
    private Sections sections;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        사당역 = new Station("사당역");
        이호선 = new Line("2호선", "green");
        sections = new Sections();
    }

    @Test
    @DisplayName("지하철 구간을 추가한다.")
    void add() {
        // when
        sections.add(new Section(이호선, 강남역, 역삼역, 10));

        // then
        assertThat(sections).isEqualTo(new Sections(Collections.singletonList(
                new Section(이호선, 강남역, 역삼역, 10)
        )));
    }

    @Test
    @DisplayName("상행 종점역을 반환한다.")
    void getUpStation() {
        // given
        sections.add(new Section(이호선, 강남역, 역삼역, 10));
        sections.add(new Section(이호선, 사당역, 강남역, 6));

        // when
        Station station = sections.getUpStation();

        // then
        assertThat(station).isEqualTo(사당역);
    }

    @Test
    @DisplayName("지하철역 목록을 반환한다.")
    void getStations() {
        // given
        sections.add(new Section(이호선, 강남역, 역삼역, 10));
        sections.add(new Section(이호선, 사당역, 강남역, 6));

        // when
        List<Station> stations = sections.getStations();

        // then
        assertThat(stations).containsExactly(사당역, 강남역, 역삼역);
    }
}
