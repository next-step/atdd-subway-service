package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("경로 관련 기능")
class PathTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");
        교대역 = Station.of(3L, "교대역");
        남부터미널역 = Station.of(4L,"남부터미널역");

        신분당선 = Line.of("신분당선", "bg-red-600", 강남역, 양재역, 9, 900);
        이호선 = Line.of("이호선", "bg-red-600", 교대역, 강남역, 50, 0);
        삼호선 = Line.of("삼호선", "bg-red-600", 교대역, 양재역, 178, 0);
    }

    @DisplayName("노선의_추가된_요금_조회 - 기본요금 (1250 + 900 = 2150)")
    @Test
    void 노선의_추가된_요금_조회() {
        // given - when
        Path actual = Path.of(Arrays.asList(강남역, 양재역), 9, 신분당선.getSections());

        // then
        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual.getFare()).isEqualTo(Fare.from(2150));
        });
    }

    @DisplayName("총_거리가_10_50_사이인_경로의_추가요금_조회 - 이용거리 50KM - 기본요금 (1250 + 800 = 2,050원)")
    @Test
    void 총_거리가_10_50_사이인_경로의_추가요금_조회() {
        // given - when
        Path actual = Path.of(Arrays.asList(교대역, 강남역), 50, 이호선.getSections());
        // then
        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual.getFare()).isEqualTo(Fare.from(2050));
        });
    }

    @DisplayName("총_거리가_50이상인_경로의_추가요금_조회 - 이용거리 178KM - 기본요금 (1250 + 2400 = 3,650원)")
    @Test
    void 총_거리가_50이상인_경로의_추가요금_조회() {
        // given - when
        Path actual = Path.of(Arrays.asList(교대역, 양재역), 178, 삼호선.getSections());

        // then
        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual.getFare()).isEqualTo(Fare.from(3650));
        });
    }
}
