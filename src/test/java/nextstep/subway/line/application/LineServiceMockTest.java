package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("노선 서비스 테스트 - 가짜 객체 사용")
@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {
	@Mock
	private LineRepository lineRepository;
	@Mock
	private StationService stationService;
	@InjectMocks
	private LineService lineService;

	private Station 종로3가역;
	private Station 신길역;
	private StationResponse 종로3가역_응답;
	private StationResponse 신길역_응답;
	private Line 일호선;
	private Line 오호선;
	private LineRequest 일호선_요청;
	private LineRequest 오호선_요청;

	@BeforeEach
	void 초기화() {
		종로3가역 = new Station("종로3가역");
		신길역 = new Station("신길역");
		종로3가역_응답 = StationResponse.of(종로3가역);
		신길역_응답 = StationResponse.of(신길역);
		일호선 = new Line("1호선", "blue", 종로3가역, 신길역, 10);
		오호선 = new Line("5호선", "purple", 종로3가역, 신길역, 10);
		일호선_요청 = new LineRequest("1호선", "blue", 1L, 2L, 10);
		오호선_요청 = new LineRequest("5호선", "purple", 종로3가역.getId(), 신길역.getId(), 10);
	}

	@Test
	void 노선_등록() {
		// given
		when(lineRepository.save(any())).thenReturn(일호선);

		// when
		LineResponse 등록_응답 = lineService.saveLine(일호선_요청);

		// then
		등록_요청_정보와_응답_정보가_같음(등록_응답);
	}

	@Test
	void 이미_등록된_노선_등록_요청하는_경우_등록되지_않음() {
		// given
		when(lineRepository.save(any()))
			.thenReturn(일호선)
			.thenReturn(new RuntimeException("이미 등록된 일호선"));
		노선_등록되어_있음(일호선_요청);

		// when

		// then
		등록되지_않음(일호선_요청);
	}

	@Test
	void 노선_목록() {
		// given
		when(lineRepository.save(any()))
			.thenReturn(일호선)
			.thenReturn(오호선);
		when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 오호선));
		LineResponse 일호선_응답 = 노선_등록되어_있음(일호선_요청);
		LineResponse 오호선_응답 = 노선_등록되어_있음(오호선_요청);

		// when
		List<LineResponse> 노선_목록 = lineService.findLines();

		// then
		지하철_노선_목록_포함됨(노선_목록, Arrays.asList(일호선_응답, 오호선_응답));
	}

	@Test
	void 노선_조회() {
		// given
		when(lineRepository.save(any())).thenReturn(일호선);
		Long 일호선_아이디 = 1L;
		when(lineRepository.findById(일호선_아이디)).thenReturn(Optional.of(일호선));
		LineResponse 일호선_응답 = 노선_등록되어_있음(일호선_요청);

		// when
		LineResponse 조회_노선 = lineService.findLineResponseById(일호선_아이디);

		// then
		등록_요청_정보와_응답_정보가_같음(조회_노선);
	}

	@Test
	void 노선_수정() {
		// given
		when(lineRepository.save(any())).thenReturn(일호선);
		Long 일호선_아이디 = null;
		when(lineRepository.findById(일호선_아이디))
			.thenReturn(Optional.of(일호선))
			.thenReturn(Optional.of(오호선));
		LineResponse 일호선_응답 = 노선_등록되어_있음(일호선_요청);

		// when
		lineService.updateLine(일호선_응답.getId(), 오호선_요청);

		// then
		LineResponse 조회_노선 = lineService.findLineResponseById(일호선_응답.getId());
		수정_요청_정보와_응답_정보가_같음(조회_노선);
	}

	private void 등록_요청_정보와_응답_정보가_같음(LineResponse 응답_정보) {
		assertThat(응답_정보.getName()).isEqualTo(일호선_요청.getName());
		assertThat(응답_정보.getColor()).isEqualTo(일호선_요청.getColor());
		assertThat(응답_정보.getStations()).containsSequence(비교할_지하철역_응답들(종로3가역, 신길역));
	}

	private List<StationResponse> 비교할_지하철역_응답들(Station... stations) {
		return Arrays.stream(stations)
			.map(StationResponse::of)
			.collect(Collectors.toList());
	}

	private LineResponse 노선_등록되어_있음(LineRequest 요청) {
		return lineService.saveLine(요청);
	}

	private void 등록되지_않음(LineRequest 일호선_요청) {
		assertThatThrownBy(() -> lineService.saveLine(일호선_요청)).isInstanceOf(RuntimeException.class);
	}

	public static void 지하철_노선_목록_포함됨(List<LineResponse> 노선_목록, List<LineResponse> 비교할_노선_목록) {
		List<Long> expectedLineIds = 노선_목록.stream()
			.map(LineResponse::getId)
			.collect(Collectors.toList());

		List<Long> resultLineIds = 비교할_노선_목록.stream()
			.map(LineResponse::getId)
			.collect(Collectors.toList());

		assertThat(resultLineIds).containsAll(expectedLineIds);
	}

	private void 수정_요청_정보와_응답_정보가_같음(LineResponse 응답_정보) {
		assertThat(응답_정보.getName()).isEqualTo(오호선_요청.getName());
		assertThat(응답_정보.getColor()).isEqualTo(오호선_요청.getColor());
		assertThat(응답_정보.getStations()).containsSequence(Arrays.asList(종로3가역_응답, 신길역_응답));
	}
}
