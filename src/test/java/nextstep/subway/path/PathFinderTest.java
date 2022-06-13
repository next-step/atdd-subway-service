package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.CannotFindPathException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("경로 조회에 대한 단위테스트")
class PathFinderTest {

    private Station 대림역;
    private Station 신대방역;
    private Station 신도림역;
    private Line 노선;

    private Station 부산역;
    private Station 대구역;
    private Line 영남선;
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        대림역 = new Station(1L, "대림");
        신대방역 = new Station(2L, "신대방");
        신도림역 = new Station(3L, "신도림");
        노선 = Line.of("2호선", "red", 대림역, 신대방역, 10, 0);

        부산역 = new Station(4L, "부산");
        대구역 = new Station(5L, "대구");
        영남선 = Line.of("영남선", "blue", 부산역, 대구역, 15, 0);
        pathFinder = new PathFinder(Arrays.asList(노선, 영남선));
    }

    @DisplayName("PathFinder 에 노선을 등록하면 해당 역들도 등록되어야 한다")
    @Test
    void register_vertex_test() {
        List<Long> stations = pathFinder.findVertexList(대림역, 신대방역);
        assertThat(stations).containsExactly(대림역.getId(), 신대방역.getId());
    }

    @DisplayName("PathFinder 에 경로를 조회하면 정상적으로 가중치가 나와야 한다")
    @Test
    void find_path_test() {
        int weight = pathFinder.getWeight(대림역, 신대방역);
        assertThat(weight).isEqualTo(10);
    }

    @DisplayName("경로 조회시 두 역이 같으면 예외가 발생한다")
    @Test
    void find_path_exception_test() {
        assertThatThrownBy(() -> {
            pathFinder.findVertexList(대림역, 대림역);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.CAN_NOT_SAME_STATION.getMessage());
    }

    @DisplayName("경로 조회시 두 역이 연결되어 있지 않으면 예외가 발생한다")
    @Test
    void find_path_exception_test2() {
        assertThatThrownBy(() -> {
            pathFinder.findVertexList(대림역, 부산역);
        }).isInstanceOf(CannotFindPathException.class)
            .hasMessageContaining(ExceptionType.IS_NOT_CONNECTED_STATION.getMessage());
    }

    @DisplayName("경로에 존재하지 않는 역을 조회하면 예외가 발생한다")
    @Test
    void find_path_exception_test3() {
        assertThatThrownBy(() -> {
            pathFinder.findVertexList(대림역, new Station("처음보는역"));
        }).isInstanceOf(CannotFindPathException.class)
            .hasMessageContaining(ExceptionType.NOT_FOUND_STATION.getMessage());
    }
}
