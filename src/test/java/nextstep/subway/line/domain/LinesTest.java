package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LinesTest {
    private Station 신논현역;
    private Station 강남역;
    private Station 선릉역;
    private Station 양재역;

    private Section 이호선_구간;
    private Section 신분당선_구간;
    private Section 신분당선_구간2;

    private Line 신분당선;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = Station.from("강남역");
        신논현역 = Station.from("신논현역");
        양재역 = Station.from("양재역");
        선릉역 = Station.from("선릉역");

        신분당선_구간 = Section.of(신논현역, 양재역, Distance.from(10));
        신분당선_구간2 = Section.of(강남역, 양재역, Distance.from(5));
        이호선_구간 = Section.of(강남역, 선릉역, Distance.from(5));

        이호선 = Line.of("이호선", "bg-green-600", 이호선_구간);
        신분당선 = Line.of("신분당선", "bg-red-600", 신분당선_구간);
        신분당선.addSection(신분당선_구간2);
    }

    @Test
    @DisplayName("지하철 역 목록 반환")
    void stations() {
        // given
        Lines lines = Lines.from(Arrays.asList(신분당선, 이호선));

        // when
        List<Station> actual = lines.stations();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(4),
                () -> assertThat(actual).contains(신논현역, 강남역, 양재역, 선릉역)
        );
    }

    @Test
    @DisplayName("구간 목록 반환")
    void sections() {
        // given
        Lines lines = Lines.from(Arrays.asList(신분당선, 이호선));

        // when
        List<Section> actual = lines.sections();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(3),
                () -> assertThat(actual).contains(신분당선_구간, 신분당선_구간2, 이호선_구간)
        );
    }
}
