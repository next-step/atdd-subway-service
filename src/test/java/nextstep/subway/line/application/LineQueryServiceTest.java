package nextstep.subway.line.application;

import nextstep.subway.line.application.exception.LineNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineQueryServiceTest {
    private LineQueryService lineQueryService;

    @Mock
    private LineRepository lineRepository;

    private Line 일호선;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        lineQueryService = new LineQueryService(lineRepository);
        Station 서울역 = new Station("upStation1");
        ReflectionTestUtils.setField(서울역, "id", 1L);
        Station 용산역 = new Station("downStation1");
        ReflectionTestUtils.setField(용산역, "id", 3L);
        Station 강남역 = new Station("upStation2");
        ReflectionTestUtils.setField(강남역, "id", 2L);
        Station 삼성역 = new Station("downStation2");
        ReflectionTestUtils.setField(삼성역, "id", 4L);
        일호선 = new Line("1호선", "blue", 서울역, 용산역, 10);
        ReflectionTestUtils.setField(일호선, "sections", new Sections(new ArrayList<>(Arrays.asList(new Section(일호선, 서울역, 용산역, 10)))));
        이호선 = new Line("2호선", "green", 강남역, 삼성역, 10);
        ReflectionTestUtils.setField(이호선, "sections", new Sections(new ArrayList<>(Arrays.asList(new Section(이호선, 강남역, 삼성역, 20)))));
    }

    @DisplayName("지하철 목록 조회를 하면 존재하는 모든 노선들을 리턴한다.")
    @Test
    void findAllLines() {
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 이호선));

        //when
        List<LineResponse> actual = lineQueryService.findLines();

        //then
        assertThat(actual).containsAll(Arrays.asList(LineResponse.of(일호선), LineResponse.of(이호선)));
    }

    @DisplayName("노선 ID를 요청하면 ID에 맞는 노선을 리턴한다.")
    @Test
    void findLine() {
        //given
        when(lineRepository.findById(anyLong())).thenReturn(Optional.of(일호선));

        //when
        LineResponse actual = lineQueryService.findLineResponseById(anyLong());

        //then
        assertAll(() -> {
            assertThat(actual.getName()).isEqualTo(일호선.getName());
            assertThat(actual.getColor()).isEqualTo(일호선.getColor());
        });
    }

    @DisplayName("요청한 노선 ID가 존재하지 않는다면 예외를 발생시킨다.")
    @Test
    void findLineNotFoundException() {
        //given
        when(lineRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> lineQueryService.findLineById(anyLong()))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessage(LineQueryService.LINE_ID_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}