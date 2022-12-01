package nextstep.subway.path.domain;

import static nextstep.subway.line.domain.LineTestFixture.line;
import static nextstep.subway.line.domain.SectionTestFixture.section;
import static nextstep.subway.station.domain.StationTestFixture.station;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {
    private Lines lines;
    private Station 선릉역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 도곡역;

    /**
     *               선릉역
     *                 |
     * 신논현역   ---   강남역   ---   양재역
     *                              |
     *                           남부터미널역
     *
     *                도곡역   ---   한티역
     */

    @BeforeEach
    void setUp() {
        선릉역 = station("선릉역");
        남부터미널역 = station("남부터미널역");
        강남역 = station("강남역");
        양재역 = station("양재역");
        도곡역 = station("도곡역");
        Station 한티역 = station("한티역");
        Station 신논현역 = station("신논현역");

        Section 신분당선_구간 = section(강남역, 양재역, 5);

        Line 이호선 = line("이호선", "bg-green-600", section(선릉역, 강남역, 3));
        Line 삼호선 = line("삼호선", "bg-orange-600", section(양재역, 남부터미널역, 5));
        Line 신분당선 = line("신분당선", "bg-red-600", section(신논현역, 양재역, 10));
        Line 수인분당선 = line("신분당선", "bg-red-600", section(도곡역, 한티역, 10));
        신분당선.addSection(신분당선_구간);

        lines = Lines.from(Arrays.asList(이호선, 삼호선, 신분당선, 수인분당선));
    }

    @Test
    @DisplayName("최단 경로 조회하기")
    void findShortestPath() {
        // given
        PathFinder pathFinder = PathFinder.from(lines);

        // when
        Path actual = pathFinder.findShortestPath(선릉역, 남부터미널역);

        // then
        assertAll(
                () -> assertThat(actual.stations()).hasSize(4),
                () -> assertThat(actual.stations()).containsExactly(선릉역, 강남역, 양재역, 남부터미널역),
                () -> assertThat(actual.distanceValue()).isEqualTo(13)
        );
    }

    @DisplayName("출발역과 도착역이 동일한 경우 최단 경로 조회 시 예외가 발생한다.")
    @Test
    void findShortestPathWithSameStation() {
        // given
        PathFinder pathFinder = PathFinder.from(lines);

        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(강남역, 강남역))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("출발역과 도착역은 같을 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 최단 경로 조회 시 예외가 발생한다.")
    @Test
    void findShortestPathWithNotConnectStation() {
        // given
        PathFinder pathFinder = PathFinder.from(lines);

        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(도곡역, 강남역))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("출발역과 도착역이 연결되지 않았습니다.");
    }
}
