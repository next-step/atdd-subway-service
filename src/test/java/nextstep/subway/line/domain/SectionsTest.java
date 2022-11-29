package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("구간관련 도메인 테스트")
public class SectionsTest {
    private Line 신분당선;
    private Station 강남역;
    private Station 양재역;
    private Station 정자역;
    private Station 광교역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        정자역 = new Station("정자역");
        광교역 = new Station("광교역");

        신분당선 = new Line("신분당선", "빨간색", 강남역, 광교역, 100);
    }

    @DisplayName("중간 구간 추가 기능 도메인 테스트")
    @Test
    void addSection1() {
        신분당선.addSection(강남역, 양재역, 20);

        List<String> stationNames = 신분당선.getStations().stream()
                .map(it -> it.getName())
                .collect(Collectors.toList());

        assertThat(stationNames).containsExactlyElementsOf(Arrays.asList(
                "강남역",
                "양재역",
                "광교역"
        ));
    }

    @DisplayName("신규 상행 종점 구간 추가 기능 도메인 테스트")
    @Test
    void addSection2() {
        신분당선.addSection(양재역, 강남역, 20);

        List<String> stationNames = 신분당선.getStations().stream()
                .map(it -> it.getName())
                .collect(Collectors.toList());

        assertThat(stationNames).containsExactlyElementsOf(Arrays.asList(
                "양재역",
                "강남역",
                "광교역"
        ));
    }

    @DisplayName("신규 하행 종점 구간 추가 기능 도메인 테스트")
    @Test
    void addSection3() {
        신분당선.addSection(광교역, 양재역, 20);

        List<String> stationNames = 신분당선.getStations().stream()
                .map(it -> it.getName())
                .collect(Collectors.toList());

        assertThat(stationNames).containsExactlyElementsOf(Arrays.asList(
                "강남역",
                "광교역",
                "양재역"
        ));
    }

    @DisplayName("중간 앞구간 추가 기능 도메인 테스트")
    @Test
    void addSection4() {
        신분당선.addSection(양재역, 광교역, 20);

        List<String> stationNames = 신분당선.getStations().stream()
                .map(it -> it.getName())
                .collect(Collectors.toList());

        assertThat(stationNames).containsExactlyElementsOf(Arrays.asList(
                "강남역",
                "양재역",
                "광교역"
        ));
    }

    @DisplayName("중간 앞구간 추가 시 거리 오류로 인한 예외 테스트")
    @Test
    void addSectionExceptionDistance() {
        assertThatThrownBy(() -> 신분당선.addSection(양재역, 광교역, 100))
                .isInstanceOf(RuntimeException.class);
    }
}
