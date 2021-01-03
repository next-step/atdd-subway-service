package nextstep.subway.path.service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
public class PathServiceRepositoryTest {

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

    @BeforeEach
    void setUp() {
        // given
        교대역 = new Station("교대역");
        양재역 = new Station("양재역");
        Station 남부터미널 = new Station("남부터미널");
        Station 강남역 = new Station("강남역");

        Line 신분당선 = new Line("신분당선", "orange", 강남역, 양재역, 5);
        Line 삼호선 = new Line("3호선", "orange", 교대역, 남부터미널, 1);
        삼호선.add(남부터미널, 양재역, 1);
        Line 이호선 = new Line("2호선", "orange", 교대역, 강남역, 5);

        lineRepository.saveAll(Arrays.asList(신분당선, 삼호선, 이호선));
    }

    @AfterEach
    void tearDown() {
        sectionRepository.deleteAllInBatch();
        lineRepository.deleteAllInBatch();
        stationRepository.deleteAllInBatch();
    }

    @DisplayName("최단 경로 조회")
    @Test
    void findPath() {
        PathResponse pathResponse = pathService.findPath(교대역.getId(), 양재역.getId());

        assertThat(pathResponse.getDistance()).isEqualTo(2);
        assertThat(pathResponse.getStations())
                .extracting("name")
                .containsExactly("교대역", "남부터미널", "양재역");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void findPathWhenNotExistStation() {
        // given
        Station 사당역 = stationRepository.save(new Station("사당역"));

        // when then
        assertThatThrownBy(() -> pathService.findPath(교대역.getId(), 사당역.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않은 출발역이나 도착역입니다.");
    }
}
