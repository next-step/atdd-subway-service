package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static nextstep.subway.line.domain.LineFixture.*;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {

    @Mock
    LineRepository lineRepository;
    @Mock
    StationService stationService;
    @InjectMocks
    LineService lineService;

    private Line 일호선;
    private Line 이호선;
    private Line 신이호선;
    private Station 서울역;
    private Station 시청역;
    private Station 남영역;

    @BeforeEach
    void setup() {
        일호선 = 일호선();
        이호선 = 이호선();
        신이호선 = 신이호선();
        남영역 = 남영역();
        서울역 = 서울역();
        시청역 = 시청역();

    }
    @DisplayName("노선을 저장한다.")
    @Test
    void 노선_저장_테스트() {
        // given
        LineRequest lineRequest = new LineRequest("1호선", "Blue", 서울역().getId(), 시청역().getId(), 20);

        given(stationService.findById(서울역.getId())).willReturn(서울역);
        given(stationService.findById(시청역.getId())).willReturn(시청역);
        given(lineRepository.save(any())).willReturn(일호선);

        // when
        LineResponse lineResponse = lineService.saveLine(lineRequest);
        List<String> stationNames = lineResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        // then
        verify(lineRepository, times(1)).save(any());
        assertThat(stationNames).containsExactly(서울역.getName(), 시청역.getName());
    }

    @DisplayName("전체 노선을 조회한다.")
    @Test
    void 전체_노선_조회_테스트() {
        // given
        given(lineRepository.findAll()).willReturn(Arrays.asList(이호선, 신이호선));

        // when
        List<LineResponse> lineResponses = lineService.findLines();

        // then
        assertThat(lineResponses).hasSize(2);
    }

    @DisplayName("노선 ID로 특정 노선을 조회한다.")
    @Test
    void 특정_노선_조회_테스트() {
        // given
        given(lineRepository.findById(이호선.getId())).willReturn(Optional.of(이호선));

        // when
        Line line = lineService.findLineById(이호선.getId());

        // then
        assertAll(
                () -> assertThat(line.getId()).isEqualTo(이호선.getId()),
                () -> assertThat(line.getName()).isEqualTo(이호선.getName()),
                () -> assertThat(line.getColor()).isEqualTo(이호선.getColor())
        );
    }

    @DisplayName("노선 ID로 특정 노선 응답값을 조회한다.")
    @Test
    void 특정_노선_응답_조회_테스트() {
        // given
        given(lineRepository.findById(이호선.getId())).willReturn(Optional.of(이호선));

        // when
        LineResponse response = lineService.findLineResponseById(이호선.getId());

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(이호선.getId()),
                () -> assertThat(response.getName()).isEqualTo(이호선.getName()),
                () -> assertThat(response.getColor()).isEqualTo(이호선.getColor())
        );
    }

    @DisplayName("노선 정보를 갱신한다.")
    @Test
    void 노선_갱신_테스트() {
        // given
        given(lineRepository.findById(일호선.getId())).willReturn(Optional.of(일호선));

        // when
        lineService.updateLine(1L, new LineRequest("3호선", "black", 서울역.getId(), 시청역.getId(), 10));

        // then
        assertAll(
                () -> assertThat(일호선.getName()).isEqualTo("3호선"),
                () -> assertThat(일호선.getColor()).isEqualTo("black")
        );
    }

    @DisplayName("노선에 역을 추가한다.")
    @Test
    void 노선에_역_추가_테스트() {
        // given
        given(stationService.findById(남영역.getId())).willReturn(남영역);
        given(stationService.findById(서울역.getId())).willReturn(서울역);
        given(lineRepository.findById(일호선.getId())).willReturn(Optional.of(일호선));

        // when
        lineService.addLineStation(일호선.getId(), new SectionRequest(남영역.getId(), 서울역.getId(), 5));

        // then
        assertThat(일호선.getStations()).contains(남영역);
    }

    @DisplayName("노선에서 역을 삭제한다.")
    @Test
    void 노선에_역_제거_테스트() {
        // given
        given(stationService.findById(남영역.getId())).willReturn(남영역);
        given(stationService.findById(서울역.getId())).willReturn(서울역);
        given(lineRepository.findById(일호선.getId())).willReturn(Optional.of(일호선));
        lineService.addLineStation(일호선.getId(), new SectionRequest(남영역.getId(), 서울역.getId(), 5));

        // when
        lineService.removeLineStation(일호선.getId(), 서울역.getId());

        // then
        assertAll(
                () -> assertThat(일호선.getStations()).hasSize(2),
                () -> assertThat(일호선.getStations()).doesNotContain(서울역)
        );
    }
}
