package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Line")
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private LineService lineService;

    private LineRequest 신분당선_생성_요청;
    private Line 신분당선;
    private Station 논현역;
    private Station 정자역;
    private Long 논현역_번호;
    private Long 정자역_번호;

    private void 노선_요청_객체_생성() {
        논현역_번호 = 1L;
        논현역 = new Station("논현역");
        정자역_번호 = 2L;
        정자역 = new Station("정자역");
        신분당선_생성_요청 = new LineRequest("신분당선", "red", 논현역_번호, 정자역_번호, 10);
        신분당선 = 신분당선_생성_요청.toEntity(논현역, 정자역);
    }

    @BeforeEach
    void setUp() {
        노선_요청_객체_생성();
    }

    @Test
    @DisplayName("노선 저장")
    void saveLine() {
        // given
        역_조회_동작_정의(논현역_번호, 논현역);
        역_조회_동작_정의(정자역_번호, 정자역);
        노선_생성_동작_정의();

        // When
        LineResponse 신분당선_생성_응답 = lineService.saveLine(신분당선_생성_요청);

        // Then
        verify(stationService, times(2)).findStationById(anyLong());
        verify(lineRepository).save(any(Line.class));

        List<String> 신분당선_역_목록 = 해당_노선에_포함된_역_목록(신분당선_생성_응답);
        assertThat(신분당선_역_목록).containsExactly(논현역.getName(), 정자역.getName());
    }

    @Test
    @DisplayName("이미 존재하는 이름을 가지는 노선 생성 요청 시 에외")
    public void throwException_WhenSaveAlreadyExistLineName() {
        // Given
        역_조회_동작_정의(논현역_번호, 논현역);
        역_조회_동작_정의(정자역_번호, 정자역);
        given(lineRepository.save(any(Line.class))).willThrow(IllegalArgumentException.class);

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> lineService.saveLine(신분당선_생성_요청));

        // Then
        verify(stationService, times(2)).findStationById(anyLong());
        verify(lineRepository).save(any(Line.class));
    }

    @Test
    @DisplayName("노선에 추가되는 역이 존재 하지 않는 경우")
    public void throwException_WhenUpStationOrDownStationIsNotExist() {
        // Given
        given(stationService.findStationById(anyLong())).willThrow(IllegalArgumentException.class);

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> lineService.saveLine(신분당선_생성_요청));

        // Then
        verify(stationService).findStationById(anyLong());
    }

    @Test
    @DisplayName("전체 노선 조회")
    public void findLines() {
        // Given
        given(lineRepository.findAll()).willReturn(Arrays.asList(신분당선));

        // When
        List<LineResponse> 전체_노선_조회_응답 = lineService.findLines();

        // Then
        verify(lineRepository).findAll();

        assertThat(전체_노선_조회_응답)
            .hasSize(1)
            .allSatisfy(it -> assertThat(it.getName()).isEqualTo(신분당선.getName()));
    }

    @Test
    @DisplayName("특정 노선 조회")
    public void findLineById() {
        // Given
        노선_조회_동작_정의(신분당선);

        // When
        LineResponse 신분당선_조회_응답 = lineService.findLineResponseById(anyLong());

        // Then
        verify(lineRepository).findById(anyLong());
        assertThat(신분당선_조회_응답.getName()).isEqualTo(신분당선.getName());
    }

    @Test
    @DisplayName("존재하지 않는 노선 조회 시 예외")
    public void throwException_WhenFindLineIsNotExist() {
        // Given
        given(lineRepository.findById(anyLong())).willThrow(IllegalArgumentException.class);

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> lineService.findLineResponseById(anyLong()));

        // Then
        verify(lineRepository).findById(anyLong());
    }

    @Test
    @DisplayName("노선 정보 수정")
    public void updateLine() {
        // Given
        String newColor = "orange";
        LineRequest 신분당선_수정_요청 = new LineRequest("신분당선", newColor, 논현역_번호, 정자역_번호, 100);

        노선_조회_동작_정의(신분당선);

        // When
        lineService.updateLine(anyLong(), 신분당선_수정_요청);

        // Then
        verify(lineRepository).findById(anyLong());
        assertThat(신분당선.getColor()).isEqualTo(신분당선_수정_요청.getColor());
    }

    @Test
    @DisplayName("노선에 구간 추가")
    public void addLineStation() {
        // Given
        Long 강남역_번호 = 3L;
        Station 강남역 = new Station("강남역");
        SectionRequest sectionRequest = new SectionRequest(논현역_번호, 강남역_번호, 5);
        노선_조회_동작_정의(신분당선);
        역_조회_동작_정의(논현역_번호, 논현역);
        역_조회_동작_정의(강남역_번호, 강남역);

        // When
        lineService.addLineStation(anyLong(), sectionRequest);

        // Then
        assertThat(신분당선.getSections()).hasSize(2);
        assertThat(신분당선.getAllStations())
            .hasSize(3)
            .containsExactly(논현역, 강남역, 정자역);
    }

    @Test
    @DisplayName("동일 구간 존재 시 예외")
    public void throwException_WhenExistSameSection() {
        // Given
        SectionRequest sectionRequest = new SectionRequest(논현역_번호, 정자역_번호, 5);
        노선_조회_동작_정의(신분당선);
        역_조회_동작_정의(논현역_번호, 논현역);
        역_조회_동작_정의(정자역_번호, 정자역);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> lineService.addLineStation(anyLong(), sectionRequest));
    }

    @Test
    @DisplayName("동일역으로 구성된 구간 추가 시 예외")
    public void throwException_When() {
        // Given
        SectionRequest sectionRequest = new SectionRequest(논현역_번호, 논현역_번호, 5);
        노선_조회_동작_정의(신분당선);
        역_조회_동작_정의(논현역_번호, 논현역);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> lineService.addLineStation(anyLong(), sectionRequest));
    }

    @Test
    @DisplayName("노선의 구간 제거")
    public void removeLineStation() {
        // Given
        Long 강남역_번호 = 3L;
        Station 강남역 = new Station("강남역");

        노선_조회_동작_정의(신분당선);
        역_조회_동작_정의(강남역_번호, 강남역);

        구간_추가(신분당선, 논현역, 강남역, 5);

        // When
        lineService.removeLineStation(anyLong(), 강남역_번호);

        // Then
        assertThat(신분당선.getSections()).hasSize(1);
        assertThat(신분당선.getAllStations()).hasSize(2).containsExactly(논현역, 정자역);
    }

    @Test
    @DisplayName("노선이 단일 구간인 경우 예외")
    public void removeLineStation_222() {
        // Given
        노선_조회_동작_정의(신분당선);
        역_조회_동작_정의(논현역_번호, 논현역);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> lineService.removeLineStation(anyLong(), 논현역_번호));
    }

    private void 역_조회_동작_정의(Long 역번호, Station 반환역) {
        given(stationService.findStationById(역번호)).willReturn(반환역);
    }

    private void 노선_생성_동작_정의() {
        given(lineRepository.save(any(Line.class))).will(AdditionalAnswers.returnsFirstArg());
    }

    private void 노선_조회_동작_정의(Line 반환_노선) {
        given(lineRepository.findById(anyLong())).willReturn(Optional.of(반환_노선));
    }

    private List<String> 해당_노선에_포함된_역_목록(LineResponse 노선_조회_응답) {
        return 노선_조회_응답.getStations()
            .stream()
            .map(it -> it.getName())
            .collect(Collectors.toList());
    }

    private void 구간_추가(Line 노선, Station 상행역, Station 하행역, int 구간) {
        노선.addSection(Section.of(노선, 상행역, 하행역, 구간));
    }
}
