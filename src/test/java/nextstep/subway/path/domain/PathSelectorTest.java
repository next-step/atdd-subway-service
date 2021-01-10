package nextstep.subway.path.domain;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


@DisplayName("구간 탐색 기능 테스트")
public class PathSelectorTest {
    private static final int DEFAULT_DISTANCE = 10;

    private static Station 신도림 = new Station("신도림");
    private static Station 문래 = new Station("문래");
    private static Station 영등포구청 = new Station("영등포구청");
    private static Station 당산 = new Station("당산");

    private static Station 국회의사당 = new Station("국회의사당");
    private static Station 여의도 = new Station("여의도");
    private static Station 샛강 = new Station("샛강");
    private static Station 노량진 = new Station("노량진");

    private static Station 영등포 = new Station("영등포");
    private static Station 신길  = new Station("신길");
    private static Station 대방  = new Station("대방");

    public PathSelectorTest() {
        Line 이호선 = new Line("2호선", "green");

        PathSelector.add(new Section(이호선, 신도림, 문래, DEFAULT_DISTANCE));
        PathSelector.add(new Section(이호선, 문래, 영등포구청, DEFAULT_DISTANCE));
        PathSelector.add(new Section(이호선, 영등포구청, 당산, DEFAULT_DISTANCE));

        Line 구호선 = new Line("9호선", "brown");

        PathSelector.add(new Section(구호선, 당산, 국회의사당, DEFAULT_DISTANCE));
        PathSelector.add(new Section(구호선, 국회의사당, 여의도, DEFAULT_DISTANCE));
        PathSelector.add(new Section(구호선, 여의도, 샛강, DEFAULT_DISTANCE));
        PathSelector.add(new Section(구호선, 샛강, 노량진, DEFAULT_DISTANCE));

        Line 일호선 = new Line("1호선", "blue");

        PathSelector.add(new Section(일호선, 신도림, 영등포, DEFAULT_DISTANCE));
        PathSelector.add(new Section(일호선, 영등포, 신길, DEFAULT_DISTANCE));
        PathSelector.add(new Section(일호선, 신길, 대방, DEFAULT_DISTANCE));
        PathSelector.add(new Section(일호선, 대방, 노량진, DEFAULT_DISTANCE));
    }

    @DisplayName("최단거리 탐색")
    @ParameterizedTest
    @MethodSource
    void selectShortestPath(Station source, Station target, Station[] expected) {
        PathResult result = PathSelector.select(source, target);

        assertThat(result.getStations()).containsExactly(expected);
        assertThat(result.getTotalDistance()).isEqualTo((expected.length - 1) * DEFAULT_DISTANCE);
    }

    private static Stream<Arguments> selectShortestPath() {
        return Stream.of(
                Arguments.of(신도림, 당산, new Station[]{신도림, 문래, 영등포구청, 당산}),
                Arguments.of(영등포구청, 노량진, new Station[]{영등포구청, 당산, 국회의사당, 여의도, 샛강, 노량진}),
                Arguments.of(문래, 노량진, new Station[]{문래, 신도림, 영등포, 신길, 대방, 노량진})
        );
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void selectNotRelatedStation() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> PathSelector.select(신도림, new Station("서울")))
                .withMessage("연결되지 않은 역은 조회 할 수 없습니다.");
    }
}
