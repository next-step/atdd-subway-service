package nextstep.subway.line.application;

import nextstep.subway.fixture.StationFixture;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.line.dto.SectionCreateRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    @Spy
    private LineService lineService;

    private Station 강남역;
    private Station 교대역;
    private Station 삼성역;
    private Station 선릉역;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        this.강남역 = StationFixture.강남역;
        this.교대역 = StationFixture.교대역;
        this.삼성역 = StationFixture.삼성역;
        this.선릉역 = StationFixture.선릉역;
        this.이호선 = new Line("2호선", "color", 강남역, 교대역, 5);
        이호선.addSection(교대역, 삼성역, 5);
    }

    /**
     * Given 지하철 노선 생성 요청이 주어지면
     * When 노선 생성시
     * Then 정상적으로 생성 후 Response를 반환한다
     */
    @DisplayName("지하철 노선 생성 - 지하철 노선 생성 요청이 주어진 경우")
    @Test
    void save_line_with_request_test() {
        // given
        long upStationId = 1L;
        long downStationId = 2L;
        LineCreateRequest request = new LineCreateRequest("2호선", "color", upStationId, downStationId, 5, 0);

        given(stationService.findStationById(upStationId)).willReturn(강남역);
        given(stationService.findStationById(downStationId)).willReturn(교대역);
        given(lineRepository.save(any())).willReturn(이호선);

        // when
        LineResponse response = lineService.saveLine(request);

        // then
        then(lineRepository).should(times(1)).save(any());
        지하철_노선_응답_검증(response, 이호선);
    }

    /**
     * When 등록 된 모든 노선을 조회하면
     * Then 모든 노선에 대한 정보를 반환한다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void find_all_lines_test() {
        // given
        given(lineRepository.findAllWithSections()).willReturn(Arrays.asList(이호선));

        // when
        List<LineResponse> responses = lineService.findLines();

        // then
        then(lineRepository).should(times(1)).findAllWithSections();
        지하철_노선_목록_응답_검증(responses, 이호선);
    }

    /**
     * Given 지하철 노선 id와 수정 요청이 주어지면
     * When 노선을 수정시
     * Then 정상적으로 수정한다
     */
    @DisplayName("지하철 노선 수정 - 노선 id와 수정 요청이 주어진 경우")
    @Test
    void update_line_test() {
        // given
        Line line = new Line("구분당선", "color");
        given(lineRepository.findByIdWithSections(any())).willReturn(Optional.of(line));
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest("신분당선", "changeColor", 0);

        // when
        lineService.updateLine(1L, lineUpdateRequest);

        // then
        then(lineRepository).should(times(1)).findByIdWithSections(any());
        assertThat(line).isEqualTo(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    /**
     * Given 지하철 노선 id가 주어지면
     * When 노선 삭제시
     * Then 정상적으로 삭제한다
     */
    @DisplayName("지하철 노선 삭제 - 노선 id가 주어진 경우")
    @Test
    void delete_line_test() {
        // given
        willDoNothing().given(lineRepository).deleteById(any());

        // when
        lineService.deleteLineById(1L);

        // then
        then(lineRepository).should(times(1)).deleteById(any());
    }

    /**
     * Given 지하철 노선 id와 구간 등록 요청이 주어지면
     * When 신규 구간 등록시
     * Then 정상적으로 구간이 등록된다
     */
    @DisplayName("지하철 구간 등록 - 노선 id와 구간 등록 요청이 주어진 경우")
    @Test
    void add_line_section_test() {
        // given
        long upStationId = 1L;
        long downStationId = 2L;
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(upStationId, downStationId, 5);

        given(lineRepository.findByIdWithSections(any())).willReturn(Optional.of(이호선));
        given(stationService.findStationById(upStationId)).willReturn(삼성역);
        given(stationService.findStationById(downStationId)).willReturn(선릉역);

        // when
        lineService.addLineStation(1L, sectionCreateRequest);

        // then
        then(stationService).should(times(2)).findStationById(any());
        assertThat(이호선.getStations()).containsExactly(강남역, 교대역, 삼성역, 선릉역);
    }

    /**
     * Given 지하철 노선 id와 제거가 필요한 역 id가 주어지면
     * When 노선에 등록 된 역 제거시
     * Then 정상적으로 제거된다
     */
    @DisplayName("지하철 노선에 등록 된 역 제거 - 노선 id와 역 id가 주어진 경우")
    @Test
    void remove_line_station_test() {
        // given
        long stationId = 1L;
        given(lineRepository.findByIdWithSections(any())).willReturn(Optional.of(이호선));
        given(stationService.findStationById(stationId)).willReturn(삼성역);

        // when
        lineService.removeLineStation(1L, stationId);

        // then
        then(stationService).should(times(1)).findStationById(any());
        assertThat(이호선.getStations()).containsExactly(강남역, 교대역);
    }

    private void 지하철_노선_응답_검증(LineResponse response, Line expectedLine) {
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(expectedLine.getName()),
                () -> assertThat(response.getColor()).isEqualTo(expectedLine.getColor())
        );
    }

    private void 지하철_노선_목록_응답_검증(List<LineResponse> responses, Line expectedLine) {
        responses.forEach(it -> 지하철_노선_응답_검증(it, expectedLine));
    }
}
