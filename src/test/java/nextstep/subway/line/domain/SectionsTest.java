package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.station.domain.Station;
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
        final Sections sections = new Sections(Arrays.asList(강남역_판교역, 판교역_광교역));

        // when
        final List<Station> stations = sections.getStations();

        // then
        assertAll(
                () -> assertThat(stations).hasSize(3),
                () -> assertThat(stations).containsExactly(강남역, 판교역, 광교역)
        );

    }
}
