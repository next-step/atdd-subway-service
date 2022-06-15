package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static nextstep.subway.path.application.PathServiceTest.getLines;
import static nextstep.subway.path.application.PathServiceTest.getStation;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("전체 노선에 대한 기능")
public class LinesTest {
    private static Station 강남역;
    private static Station 양재역;
    private static Station 수진역;

    private Lines lines;

    @BeforeEach
    void setUp() {
        lines = new Lines(getLines());
        강남역 = getStation(1L, "강남역");
        양재역 = getStation(2L, "양재역");
        수진역 = getStation(5L, "수진역");
    }

    @Test
    void 전체_노선에_등록된_역을_조회한다() {
        // when
        List<Station> stations =  lines.getAllStations();

        // then
        assertThat(stations).hasSize(6);
    }

    @Test
    void 전체_노선에_등록된_구간을_조회한다() {
        // when
        List<Section> sections = lines.getAllSections();

        // then
        assertThat(sections).hasSize(5);
    }

    @ParameterizedTest
    @MethodSource("노선에_포함되거나_포함되지_않은_지하철역을_가져온다")
    void 노선에_지하철_역이_모두_포함되어_있는지_확인한다(List<Station> stations, boolean expected) {
        // when
        boolean result = lines.notContainsAll(stations.get(0), stations.get(1));

        // then
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> 노선에_포함되거나_포함되지_않은_지하철역을_가져온다() {
        return Stream.of(
                Arguments.of(
                        포함된_역(),
                        false
                ),
                Arguments.of(
                        포함되지_않은_역(),
                        true
                )
        );
    }

    private static List<Station> 포함된_역() {
        return Arrays.asList(
                강남역, 양재역
        );
    }

    private static List<Station> 포함되지_않은_역() {
        return Arrays.asList(
                강남역, 수진역
        );
    }
}
