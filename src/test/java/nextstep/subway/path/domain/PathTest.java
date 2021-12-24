package nextstep.subway.path.domain;

import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("경로 관련 기능")
public class PathTest {

    @DisplayName("경로 생성")
    @Test
    public void 경로_생성() {
        // given
        Station 교대역 = new Station("교대역");
        Station 신논현역 = new Station("신논현역");

        // when
        Path 경로 = new Path(교대역, 신논현역);

        // then
        assertThat(경로).isNotNull();
    }

    @DisplayName("경로 실패")
    @Test
    public void 경로_실패() {
        // given
        Station 교대역 = new Station("교대역");

        // then
        assertThatThrownBy(() -> new Path(교대역, 교대역))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("출발역과 도착역은 동일할 수 없습니다.");
    }

    @DisplayName("최단경로 조회")
    @Test
    public void 최단경로_조회() {
        // given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 교대역, 10);
        Station 양재역 = new Station("양재역");
        Section 강남_양재_구간 = Section.of(신분당선, 강남역, 양재역, Distance.of(3));
        신분당선.addSection(강남_양재_구간);

        Station 신논현역 = new Station("신논현역");
        Station 종합운동장역 = new Station("종합운동장역");
        Line 구호선 = new Line("구호선", "bg-red-600", 강남역, 신논현역, 10);
        Section 종합운동장_신논현_구간 = Section.of(구호선, 신논현역, 종합운동장역, Distance.of(5));
        구호선.addSection(종합운동장_신논현_구간);

        // when
        Path 경로 = new Path(교대역, 신논현역);
        Lines lines = new Lines(Arrays.asList(신분당선, 구호선));
        List<Station> 최단경로_역_목록 = 경로.findShortestPath(lines);

        // then
        assertAll(
                () -> assertThat(최단경로_역_목록).isNotNull(),
                () -> assertThat(최단경로_역_목록.size()).isEqualTo(4),
                () -> assertThat(최단경로_역_목록).contains(교대역, 양재역, 강남역, 신논현역)
        );
    }

    @DisplayName("연결되지 않은 노선의 최단경로 조회 실패")
    @Test
    public void 연결되지_않은_노선의_최단경로_조회_실패() {
        // given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 교대역, 10);
        Station 양재역 = new Station("양재역");
        Section 강남_양재_구간 = Section.of(신분당선, 강남역, 양재역, Distance.of(3));
        신분당선.addSection(강남_양재_구간);

        Station 봉은사역 = new Station("봉은사역");
        Station 신논현역 = new Station("신논현역");
        Station 종합운동장역 = new Station("종합운동장역");
        Line 구호선 = new Line("구호선", "bg-red-600", 봉은사역, 신논현역, 10);
        Section 종합운동장_신논현_구간 = Section.of(구호선, 신논현역, 종합운동장역, Distance.of(5));
        구호선.addSection(종합운동장_신논현_구간);

        // when
        Path 경로 = new Path(교대역, 신논현역);
        Lines lines = new Lines(Arrays.asList(신분당선, 구호선));

        // then
        assertThatThrownBy(() -> 경로.findShortestPath(lines))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("출발역과 도착역이 연결되지 않았습니다.");
    }

}
