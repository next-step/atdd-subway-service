package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("경로상 사용하는 노선을 찾는 기능 테스트")
public class ThroughLineSelectorTest extends PathDomainBase {

    public ThroughLineSelectorTest() {
        이호선.addSection(new Section(이호선, 신도림, 문래, DEFAULT_DISTANCE));
        이호선.addSection(new Section(이호선, 문래, 영등포구청, DEFAULT_DISTANCE));
        이호선.addSection(new Section(이호선, 영등포구청, 당산, DEFAULT_DISTANCE));

        구호선.addSection(new Section(구호선, 당산, 국회의사당, DEFAULT_DISTANCE));
        구호선.addSection(new Section(구호선, 국회의사당, 여의도, DEFAULT_DISTANCE));
        구호선.addSection(new Section(구호선, 여의도, 샛강, DEFAULT_DISTANCE));
        구호선.addSection(new Section(구호선, 샛강, 노량진, DEFAULT_DISTANCE));

        일호선.addSection(new Section(일호선, 신도림, 영등포, DEFAULT_DISTANCE));
        일호선.addSection(new Section(일호선, 영등포, 신길, DEFAULT_DISTANCE));
        일호선.addSection(new Section(일호선, 신길, 대방, DEFAULT_DISTANCE));
        일호선.addSection(new Section(일호선, 대방, 노량진, DEFAULT_DISTANCE));
    }

    @DisplayName("경로상 사용하는 노선을 찾는 기능")
    @ParameterizedTest
    @MethodSource
    void findThroughLine(ArgumentSupplier<List<Long>> stationIdsSupplier, ArgumentSupplier<Line[]> expectedSupplier) {
        List<Line> lines = Arrays.asList(일호선, 이호선, 구호선);
        List<Long> stationdIds = stationIdsSupplier.get();
        Line[] expected = expectedSupplier.get();

        ThroughLineSelector throughLineSelector = new ThroughLineSelector(lines, stationdIds);
        List<Line> throughLines = throughLineSelector.find();

        assertThat(throughLines).contains(expected);
    }

    private static Stream<Arguments> findThroughLine() {
        return Stream.of(
                Arguments.of(supply(() -> Arrays.asList(신도림.getId(), 문래.getId(), 영등포구청.getId(), 당산.getId())), supply(() -> new Line[]{이호선})),
                Arguments.of(supply(() -> Arrays.asList(신도림.getId(), 영등포.getId(), 신길.getId())), supply(() -> new Line[]{일호선})),
                Arguments.of(supply(() -> Arrays.asList(당산.getId(), 국회의사당.getId(), 여의도.getId())), supply(() -> new Line[]{구호선})),
                Arguments.of(supply(() -> Arrays.asList(영등포구청.getId(), 당산.getId(), 국회의사당.getId())), supply(() -> new Line[]{이호선,구호선})),
                Arguments.of(supply(() -> Arrays.asList(영등포구청.getId(), 당산.getId(), 국회의사당.getId(), 여의도.getId(), 샛강.getId(), 노량진.getId(), 대방.getId())), supply(() -> new Line[]{일호선, 이호선,구호선}))
        );
    }

}
