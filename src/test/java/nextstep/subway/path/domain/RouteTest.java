package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.fixture.LineFixture;
import nextstep.subway.path.domain.fixture.SubwayGraphFixture;
import nextstep.subway.station.domain.fixture.StationFixture;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class RouteTest {
    private Route 경로;

    @BeforeEach
    void setUp() {
        LineFixture.신분당선.getSections().clear();
        LineFixture.삼호선.getSections().clear();

        Line 신분당선 = SubwayGraphFixture.신분당선;
        Line 삼호선 = SubwayGraphFixture.삼호선;

        경로 = Route.of(Lists.newArrayList(신분당선, 삼호선));
    }

    @DisplayName("경로 조회하기 예외 발생 - 출발역과 도착역이 같음")
    @Test
    void 경로_조회하기_예외_발생_출발역과_도착역이_같음() {
        assertThatThrownBy(() -> 경로.findPath(StationFixture.교대역, StationFixture.교대역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 같습니다.");
    }

    @DisplayName("경로 조회하기")
    @Test
    void 경로_조회하기() {
        // when
        Path path = 경로.findPath(StationFixture.교대역, StationFixture.판교역);

        // then
        assertThat(path.getStations()).containsExactly(
                StationFixture.교대역,
                StationFixture.남부터미널역,
                StationFixture.양재역,
                StationFixture.양재시민의숲역,
                StationFixture.청계산입구역,
                StationFixture.판교역
        );

        assertThat(path.getDistance()).isEqualTo(19);
    }
}
