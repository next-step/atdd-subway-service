package nextstep.subway.line.application;

import static nextstep.subway.generator.StationGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("노선 Service 테스트")
@ExtendWith(MockitoExtension.class)
class LineServiceTest {

	@Mock
	private LineRepository lineRepository;

	@Mock
	private StationService stationService;

	@InjectMocks
	private LineService lineService;

	private LineCreateRequest 이호선_생성_요청;
	private Line 이호선;
	private Station 강남역;
	private Station 역삼역;
	private Long 강남역_번호;
	private Long 역삼역_번호;

	@BeforeEach
	void setUp() {
		초기_노선_생성();
	}

	private void 초기_노선_생성() {
		강남역_번호 = 1L;
		역삼역_번호 = 2L;
		강남역 = station("강남역");
		역삼역 = station("역삼역");

		이호선_생성_요청 = new LineCreateRequest("이호선", "green", 강남역_번호, 역삼역_번호, 10);
		이호선 = Line.of("이호선", "green", 강남역, 역삼역, 10);
	}

	@Test
	@DisplayName("노선 저장")
	void saveLineTest() {
		// given
		검색된_지하철역(강남역_번호, 강남역);
		검색된_지하철역(역삼역_번호, 역삼역);
		저장된_노선(이호선);

		// when
		LineResponse 이호선_생성_응답 = lineService.saveLine(이호선_생성_요청);

		// then
		verify(stationService, times(2)).findStationById(anyLong());
		verify(lineRepository, times(1)).save(any(Line.class));

		assertThat(노선에_속한_역_목록(이호선_생성_응답)).containsExactly("강남역", "역삼역");
	}

	private List<String> 노선에_속한_역_목록(LineResponse response) {
		return response.getStations().stream()
			.map(StationResponse::getName)
			.collect(Collectors.toList());

	}

	private void 저장된_노선(Line line) {
		when(lineRepository.save(any(Line.class))).thenReturn(line);
	}

	private void 검색된_지하철역(Long id, Station station) {
		when(stationService.findStationById(id)).thenReturn(station);
	}

	private void 생성된_노선() {
		when(lineRepository.save(any(Line.class))).then(AdditionalAnswers.returnsFirstArg());
	}



}