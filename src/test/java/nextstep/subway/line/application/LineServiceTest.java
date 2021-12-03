package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    LineRepository lineRepository;

    @Mock
    StationService stationService;

    @InjectMocks
    LineService lineService;

    @Test
    void saveLine() {
        // given 지하철 역 저장되어 있음
        Station 서울역 = new Station("서울역");
        Station 용산역 = new Station("용산역");
        LineRequest lineRequest = new LineRequest("1호선", "blue", 1L, 2L, 3);

        when(stationService.findById(ArgumentMatchers.anyLong()))
            .thenReturn(서울역);
        when(lineRepository.save(ArgumentMatchers.any()))
            .thenReturn(new Line("1호선", "blue", 서울역, 용산역, 3));

        // when 라인 저장 요청
        LineResponse lineResponse = lineService.saveLine(lineRequest);

        // then 라인 저장되어 목록 조회
        assertNotNull(lineResponse);
        assertAll(() -> {
            assertEquals(lineResponse.getName(), "1호선");
            assertThat(lineResponse.getStations())
                .extracting(StationResponse::getName)
                .containsExactly("서울역", "용산역");
        });
    }
}