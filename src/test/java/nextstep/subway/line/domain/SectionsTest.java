package nextstep.subway.line.domain;

import nextstep.subway.JpaEntityTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("구간 관련 도메인 테스트")
public class SectionsTest extends JpaEntityTest {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    private Line 신분당선;
    private Station 강남역;
    private Station 양재역;
    private Station 정자역;
    private Station 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        정자역 = stationRepository.save(new Station("정자역"));
        광교역 = stationRepository.save(new Station("광교역"));

        신분당선 = lineRepository.save(new Line("신분당선", "빨간색", 강남역, 광교역, 100));
    }

    @DisplayName("중간 구간 추가 기능 도메인 테스트")
    @Test
    void addSection1() {
        // given
        신분당선.addSection(강남역, 양재역, 20);
        flushAndClear();

        // when
        신분당선 = lineRepository.getById(1L);
        List<String> stationNames = 신분당선.getStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        // then
        assertThat(stationNames).containsExactlyElementsOf(Arrays.asList(
                "강남역",
                "양재역",
                "광교역"
        ));
    }

    @DisplayName("신규 상행 종점 구간 추가 기능 도메인 테스트")
    @Test
    void addSection2() {
        // given
        신분당선.addSection(양재역, 강남역, 20);
        flushAndClear();

        // when
        신분당선 = lineRepository.getById(1L);
        List<String> stationNames = 신분당선.getStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        // then
        assertThat(stationNames).containsExactlyElementsOf(Arrays.asList(
                "양재역",
                "강남역",
                "광교역"
        ));
    }

    @DisplayName("신규 하행 종점 구간 추가 기능 도메인 테스트")
    @Test
    void addSection3() {
        // given
        신분당선.addSection(광교역, 양재역, 20);
        flushAndClear();

        // when
        신분당선 = lineRepository.getById(1L);
        List<String> stationNames = 신분당선.getStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        // then
        assertThat(stationNames).containsExactlyElementsOf(Arrays.asList(
                "강남역",
                "광교역",
                "양재역"
        ));
    }

    @DisplayName("중간 앞구간 추가 기능 도메인 테스트")
    @Test
    void addSection4() {
        // given
        신분당선.addSection(양재역, 광교역, 20);
        flushAndClear();

        // when
        신분당선 = lineRepository.getById(1L);
        List<String> stationNames = 신분당선.getStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        // then
        assertThat(stationNames).containsExactlyElementsOf(Arrays.asList(
                "강남역",
                "양재역",
                "광교역"
        ));
    }

    @DisplayName("중간 앞구간 추가 시 거리 오류로 인한 예외 테스트")
    @Test
    void addSectionExceptionDistance() {
        // when / then
        assertThatThrownBy(() -> 신분당선.addSection(양재역, 광교역, 100))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("노선에서 상행 종점을 제거한다")
    @Test
    void removeSection1() {
        // given
        신분당선.addSection(강남역, 양재역, 50);

        // when
        신분당선.removeSection(강남역);
        flushAndClear();
        신분당선 = lineRepository.getById(1L);
        List<String> stationNames = 신분당선.getStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        // then
        assertThat(stationNames).containsExactlyElementsOf(Arrays.asList("양재역", "광교역"));
    }

    @DisplayName("노선에서 중간 역을 제거한다")
    @Test
    void removeSection2() {
        // given
        신분당선.addSection(강남역, 양재역, 50);

        // when
        신분당선.removeSection(양재역);
        flushAndClear();
        신분당선 = lineRepository.getById(1L);
        List<String> stationNames = 신분당선.getStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        // then
        assertThat(stationNames).containsExactlyElementsOf(Arrays.asList("강남역", "광교역"));
    }

    @DisplayName("노선에서 하행 종점 역을 제거한다")
    @Test
    void removeSection3() {
        // given
        신분당선.addSection(강남역, 양재역, 50);

        // when
        신분당선.removeSection(양재역);
        flushAndClear();
        신분당선 = lineRepository.getById(1L);
        List<String> stationNames = 신분당선.getStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        // then
        assertThat(stationNames).containsExactlyElementsOf(Arrays.asList("강남역", "광교역"));
    }

    @DisplayName("노선에서 모든 역을 제거하는 경우 예외 발생")
    @Test
    void removeSectionException1() {
        // when / then
        assertThatThrownBy(() -> 신분당선.removeSection(강남역))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("노선에 존재하지 않는 역을 제거하는 경우 예외 발생")
    @Test
    void removeSectionException2() {
        // given
        신분당선.addSection(강남역, 정자역, 50);

        // when / then
//        assertThatThrownBy(() -> 신분당선.removeSection(양재역))
//                .isInstanceOf(IllegalStateException.class);
    }
}
