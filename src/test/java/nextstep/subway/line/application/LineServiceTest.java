package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static nextstep.subway.line.domain.LineFixture.신이호선;
import static nextstep.subway.line.domain.LineFixture.이호선;
import static nextstep.subway.station.StationFixture.당산역;
import static nextstep.subway.station.StationFixture.합정역;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Service
public class LineServiceTest {

    LineRepository lineRepository = mock(LineRepository.class);
    StationService stationService = mock(StationService.class);
    LineService lineService = new LineService(lineRepository, stationService);

    @DisplayName("노선을 저장한다.")
    @Test
    void 노선_저장_테스트() {
        // given
        LineRequest lineRequest = new LineRequest("2호선", "Green", 당산역.getId(), 합정역.getId(), 20);

        when(stationService.findById(당산역.getId())).thenReturn(당산역);
        when(stationService.findById(합정역.getId())).thenReturn(합정역);
        when(lineRepository.save(lineRequest.toLine(당산역, 합정역))).thenReturn(이호선());

        // when
        LineResponse lineResponse = lineService.saveLine(lineRequest);

        // then
        assertThat(lineResponse.getStations()).containsExactly(StationResponse.of(당산역), StationResponse.of(합정역));
    }

    @DisplayName("전체 노선을 조회한다.")
    @Test
    void 전체_노선_조회_테스트() {
        // given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선(), 신이호선()));

        // when
        List<LineResponse> lineResponses = lineService.findLines();

        // then
        assertThat(lineResponses).hasSize(2);
    }

    @DisplayName("노선 ID로 특정 노선을 조회한다.")
    @Test
    void 특정_노선_조회_테스트() {
        // given
        when(lineRepository.findById(이호선().getId())).thenReturn(Optional.of(이호선()));

        // when
        Line line = lineService.findLineById(이호선().getId());

        // then
        assertAll(
                () -> assertThat(line.getId()).isEqualTo(이호선().getId()),
                () -> assertThat(line.getName()).isEqualTo(이호선().getName()),
                () -> assertThat(line.getColor()).isEqualTo(이호선().getColor())
        );
    }

    @DisplayName("노선 ID로 특정 노선 응답값을 조회한다.")
    @Test
    void 특정_노선_응답_조회_테스트() {
        // given
        when(lineRepository.findById(이호선().getId())).thenReturn(Optional.of(이호선()));

        // when
        LineResponse response = lineService.findLineResponseById(이호선().getId());

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(이호선().getId()),
                () -> assertThat(response.getName()).isEqualTo(이호선().getName()),
                () -> assertThat(response.getColor()).isEqualTo(이호선().getColor())
        );
    }
}
