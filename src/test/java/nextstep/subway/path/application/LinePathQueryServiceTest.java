package nextstep.subway.path.application;


import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.LinePathRequest;
import nextstep.subway.path.dto.LinePathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class LinePathQueryServiceTest {
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    private LinePathQueryService linePathQueryService;

    private Station 강남역;
    private Station 양재역;
    private Station 정자역;
    private Station 광교역;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    public void setUp() {
        this.linePathQueryService = new LinePathQueryService(lineRepository, stationRepository);

        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        정자역 = stationRepository.save(new Station("정자역"));
        광교역 = stationRepository.save(new Station("광교역"));

        신분당선 = lineRepository.save(new Line("신분당선", "빨간색", 강남역, 양재역, 3));
        이호선 = lineRepository.save(new Line("2호선", "초록색", 강남역, 정자역, 7));
        삼호선 = lineRepository.save(new Line("3호선", "주황색", 강남역, 광교역, 2));
    }

    @Test
    @DisplayName("강남역 - 정자역 최단거리를 찾는다")
    void 강남역_정자역_최단거리를_찾는다() {
        // given
        LinePathRequest linePathRequest = new LinePathRequest(강남역.getId(), 정자역.getId());

        // when
        LinePathResponse linePathResponse = linePathQueryService.findShortDistance(linePathRequest);

        // then
        assertThat(linePathResponse.getStations())
                .map(StationResponse::getId)
                .containsExactly(강남역.getId(), 광교역.getId());

        assertThat(linePathResponse.getDistance())
                .isEqualTo(3);
    }

    @Test
    @DisplayName("존재하지 않는 역의 최단거리를 찾으려 하면 EntityNotFoundException이 발생한다")
    void 존재하지_않는_역의_최단거리를_찾으려_하면_EntityNotFoundException이_발생한다() {
        LinePathRequest linePathRequest = new LinePathRequest(1000L, 1001L);

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> linePathQueryService.findShortDistance(linePathRequest));
    }

}