package nextstep.subway.path.service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
public class PathIntegrationTest {

    @Autowired
    PathService pathService;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    SectionRepository sectionRepository;

    private Station 교대역;
    private Station 양재역;
    private Station 사당역;

    @BeforeEach
    void setUp() {
        // given
        교대역 = new Station("교대역");
        양재역 = new Station("양재역");
        Station 남부터미널 = new Station("남부터미널");
        Station 강남역 = new Station("강남역");
        사당역 = new Station("이수역");
        Station 이수역 = new Station("사당역");

        Line 신분당선 = new Line("신분당선", "orange", 강남역, 양재역, 5);
        Line 삼호선 = new Line("3호선", "orange", 교대역, 남부터미널, 1);
        삼호선.add(남부터미널, 양재역, 1);
        Line 이호선 = new Line("2호선", "orange", 교대역, 강남역, 5);
        Line 사호선 = new Line("사호선", "orange", 사당역, 이수역, 5);

        lineRepository.saveAll(Arrays.asList(신분당선, 삼호선, 이호선, 사호선));
    }

    @AfterEach
    void tearDown() {
        sectionRepository.deleteAllInBatch();
        lineRepository.deleteAllInBatch();
        stationRepository.deleteAllInBatch();
    }

    @DisplayName("지하철 경로를 조회하고 순서, 거리가 일치하는지 확인한다.")
    @Test
    void findPath() {
        PathResponse pathResponse = pathService.findPath(교대역.getId(), 양재역.getId());

        assertThat(pathResponse.getDistance()).isEqualTo(2);
        assertThat(pathResponse.getStations())
                .extracting("name")
                .containsExactly("교대역", "남부터미널", "양재역");
    }

    @DisplayName("경로 조회시 존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void findPathWhenNotExistStation() {
        // given
        Station 서울역 = stationRepository.save(new Station("서울역"));

        // when then
        assertThatThrownBy(() -> pathService.findPath(교대역.getId(), 서울역.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않은 출발역이나 도착역입니다.");
    }

    @DisplayName("경로 조회시 출발역과 도착역이 같은 경우")
    @Test
    void findPathWhenEqualStation() {
        // when then
        assertThatThrownBy(() -> pathService.findPath(교대역.getId(), 교대역.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 같습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void findPathWhenNotConnectedStation() {
        // when then
        assertThatThrownBy(() -> pathService.findPath(사당역.getId(), 교대역.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 연결 되어 있지 않습니다.");
    }
}
