package nextstep.subway.line.domain;

import nextstep.subway.path.domain.PathDomainBase;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("경로상 사용하는 노선을 찾는 기능 테스트")
public class LinesTest extends PathDomainBase {

    public LinesTest() {
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
    void findThroughLine(ArgumentSupplier<List<Station>> stationsSupplier, ArgumentSupplier<Line[]> expectedSupplier) {
        Lines lines = new Lines(Arrays.asList(일호선, 이호선, 구호선));
        List<Station> stations = stationsSupplier.get();
        Line[] expected = expectedSupplier.get();

        Set<Line> throughLines = lines.findThroughLines(stations);

        assertThat(throughLines).contains(expected);
    }

    private static Stream<Arguments> findThroughLine() {
        return Stream.of(
                Arguments.of(supply(() -> Arrays.asList(신도림, 문래, 영등포구청, 당산)), supply(() -> new Line[]{이호선})),
                Arguments.of(supply(() -> Arrays.asList(신도림, 영등포, 신길)), supply(() -> new Line[]{일호선})),
                Arguments.of(supply(() -> Arrays.asList(당산, 국회의사당, 여의도)), supply(() -> new Line[]{구호선})),
                Arguments.of(supply(() -> Arrays.asList(영등포구청, 당산, 국회의사당)), supply(() -> new Line[]{이호선,구호선})),
                Arguments.of(supply(() -> Arrays.asList(영등포구청, 당산, 국회의사당, 여의도, 샛강, 노량진, 대방)), supply(() -> new Line[]{일호선, 이호선,구호선}))
        );
    }

}
