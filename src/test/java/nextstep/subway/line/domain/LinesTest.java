package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

@DisplayName("지하철 노선들 관련 기능")
class LinesTest {

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

        신분당선 = Line.of("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = Line.of("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = Line.of("삼호선", "bg-red-600", 교대역, 양재역, 5);

        삼호선.addSection(교대역, 남부터미널역, 3);
    }

    @Test
    void 지하철_노선들_생성() {
        // given - when
        Lines lines = Lines.from(Arrays.asList(신분당선, 이호선, 삼호선));

        // then
        Assertions.assertThat(lines).isNotNull();
    }

    @Test
    void 지하철_노선들의_역을_중복제거하여_전체_조회() {
        // given
        Lines lines = Lines.from(Arrays.asList(신분당선, 이호선, 삼호선));

        // when
        List<Station> actual = lines.getStations();

        // then
        Assertions.assertThat(actual).hasSize(4);
    }

    @Test
    void 지하철_노선들의_구간을_전체_조회() {
        // given
        Lines lines = Lines.from(Arrays.asList(신분당선, 이호선, 삼호선));

        // when
        List<Section> actual = lines.getSections();

        // then
        Assertions.assertThat(actual).hasSize(4);
    }
}
