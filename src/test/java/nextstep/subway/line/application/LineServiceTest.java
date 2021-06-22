package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;

@DisplayName("LineService 협력객체 이용 테스트")
@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @BeforeEach
    void setup() {
        lineService = new LineService(lineRepository, stationService);
    }

    @DisplayName("라인 저장")
    @Test
    void save() {
        //given
        //when
        when(lineRepository.save(any(Line.class)))
                .thenReturn(new Line());
        //then
        assertThat(lineService.saveLine(new LineRequest())).isNotNull();
    }

    @DisplayName("라인을 id로 찾기")
    @Test
    void findLineById() {
        //given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(new Line()));
        //when
        Line line = lineService.findLineById(1L);
        //then
        assertThat(line).isNotNull();
    }

    @DisplayName("응답을 id로 찾기")
    @Test
    void findLineResponseById() {
        //given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(new Line()));
        //when
        LineResponse lineResponseById = lineService.findLineResponseById(1L);
        //then
        assertThat(lineResponseById).isNotNull();
    }

    @DisplayName("라인 제거")
    @Test
    void delete() {
        //given
        //when
        lineService.deleteLineById(anyLong());
        //then
        verify(lineRepository, times(1)).deleteById(anyLong());
    }
}