package nextstep.subway.path.domain;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


@DisplayName("구간 탐색 기능 테스트")
public class PathSelectorTest extends PathDomainBase {

    public PathSelectorTest() {
        PathSelector.add(new Section(이호선, 신도림, 문래, DEFAULT_DISTANCE));
        PathSelector.add(new Section(이호선, 문래, 영등포구청, DEFAULT_DISTANCE));
        PathSelector.add(new Section(이호선, 영등포구청, 당산, DEFAULT_DISTANCE));

        PathSelector.add(new Section(구호선, 당산, 국회의사당, DEFAULT_DISTANCE));
        PathSelector.add(new Section(구호선, 국회의사당, 여의도, DEFAULT_DISTANCE));
        PathSelector.add(new Section(구호선, 여의도, 샛강, DEFAULT_DISTANCE));
        PathSelector.add(new Section(구호선, 샛강, 노량진, DEFAULT_DISTANCE));

        PathSelector.add(new Section(일호선, 신도림, 영등포, DEFAULT_DISTANCE));
        PathSelector.add(new Section(일호선, 영등포, 신길, DEFAULT_DISTANCE));
        PathSelector.add(new Section(일호선, 신길, 대방, DEFAULT_DISTANCE));
        PathSelector.add(new Section(일호선, 대방, 노량진, DEFAULT_DISTANCE));
    }

    @DisplayName("최단거리 탐색")
    @ParameterizedTest
    @MethodSource
    void selectShortestPath(ArgumentSupplier<Station> sourceSupplier, ArgumentSupplier<Station> targetSupplier, ArgumentSupplier<Long[]> expectedSupplier) {
        Station source = sourceSupplier.get();
        Station target = targetSupplier.get();
        Long[] expected = expectedSupplier.get();

        PathResult result = PathSelector.select(source, target);

        assertThat(result.getStationIds()).containsExactly(expected);
        assertThat(result.getTotalDistance()).isEqualTo((expected.length - 1) * DEFAULT_DISTANCE);
    }

    private static Stream<Arguments> selectShortestPath() {
        return Stream.of(
                Arguments.of(supply(()->신도림), supply(() ->당산), supply(() -> new Long[]{신도림.getId(), 문래.getId(), 영등포구청.getId(), 당산.getId()})),
                Arguments.of(supply(()->영등포구청), supply(() ->노량진), supply(() -> new Long[]{영등포구청.getId(), 당산.getId(), 국회의사당.getId(), 여의도.getId(), 샛강.getId(), 노량진.getId()})),
                Arguments.of(supply(()->문래), supply(() ->노량진), supply(() -> new Long[]{문래.getId(), 신도림.getId(), 영등포.getId(), 신길.getId(), 대방.getId(), 노량진.getId()}))
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
