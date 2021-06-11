package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class LineServiceTest {
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    private LineService lineService;

    private Station 강남역;
    private Station 양재역;
    private Station 판교역;
    private Station 정자역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");
        정자역 = new Station("정자역");

        lineService = new LineService(lineRepository, new StationService(stationRepository));
    }

    @Test
    @DisplayName("저장을 하면 정렬된 역의 Response가 같이 나온다")
    void 저장을_하면_정렬된_역의_Response가_같이_나온다() {
        // given
        stationRepository.saveAll(Arrays.asList(양재역, 판교역));
        LineRequest lineRequest = new LineRequest("신분당선", "빨간색", 양재역.getId(), 판교역.getId(), 3);

        // when
        LineResponse lineResponse = lineService.saveLine(lineRequest);

        // then
        assertThat(lineResponse.getName()).isEqualTo(lineRequest.getName());
        assertThat(lineResponse.getColor()).isEqualTo(lineRequest.getColor());
        assertThat(lineResponse.getStations())
                .map(StationResponse::getId)
                .containsExactly(양재역.getId(), 판교역.getId());
    }


}