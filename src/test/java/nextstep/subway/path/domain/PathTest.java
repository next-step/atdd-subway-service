package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("경로 관련 기능")
class PathTest {

    private Station 강남역;
    private Station 양재역;

    private Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");

        신분당선 = Line.of("신분당선", "bg-red-600", 강남역, 양재역, 9, 900);
    }

    @Test
    void 경로_생성() {
        // given - when
        Path actual = Path.of(Arrays.asList(강남역, 양재역), 9, 신분당선.getSections());

        // then
        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual.getStations()).containsExactlyElementsOf(Arrays.asList(강남역, 양재역));
            assertThat(actual.getDistance()).isEqualTo(Distance.from(9));
            assertThat(actual.getSections().getOrderedSections()).isEqualTo(신분당선.getSections());
        });
    }
}
