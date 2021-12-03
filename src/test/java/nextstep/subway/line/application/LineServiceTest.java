package nextstep.subway.line.application;

import static nextstep.subway.line.domain.StationFixtures.잠실;
import static nextstep.subway.line.domain.StationFixtures.잠실나루;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단위 테스트 - mockito의 MockitoExtension을 활용한 가짜 협력 객체 사용")
@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private StationService stationService;

    private LineServiceV2 lineService;
    private LineRequest request;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        // given
        lineService = new LineServiceV2(lineRepository, stationService);
        request = new LineRequest("2호선", "RED", 1L, 2L, 100);
        이호선 = new Line(request.getName(), request.getColor(), 잠실, 잠실나루,
            request.getDistance());
    }

    @Test
    @DisplayName("노선 생성시 상행종점, 하행종점 같이 생성됨")
    void saveLine() {
        // when
        when(lineRepository.save(any())).thenReturn(이호선);
        LineResponse response = lineService.saveLine(request);

        // then
        assertThat(response.getStations()).extracting("name")
            .containsExactly(잠실.getName(), 잠실나루.getName());
    }

    @Test
    @DisplayName("노선 전체 조회")
    void findLines() {
        // when
        when(lineRepository.findAll()).thenReturn(Collections.singletonList(이호선));
        List<LineResponse> lines = lineService.findLines();

        // then
        assertThat(lines).hasSize(1);
    }

    @Test
    @DisplayName("노선 조회")
    void findLineById() {
        // when
        when(lineRepository.findById(any())).thenReturn(Optional.of(이호선));
        Line actual = lineService.findLineById(1L);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(이호선.getName());
    }

    @Test
    @DisplayName("없는 노선 조회 에러 발생")
    void findLineById_notFoundException() {
        // when
        // then
        assertThrows(NotFoundException.class, () -> lineService.findLineById(1L));
    }

    @Test
    @DisplayName("노선 조회 `LineResponse` 검증")
    void findLineById_to_LineResponse() {
        // when
        when(lineRepository.findById(any())).thenReturn(Optional.of(이호선));
        LineResponse actual = lineService.findLineResponseById(any());

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getStations()).hasSize(이호선.getStations().size()),
            () -> assertThat(actual.getName()).isEqualTo(이호선.getName())
        );
    }
}
