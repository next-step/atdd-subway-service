package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import nextstep.subway.ServiceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("노선 서비스 테스트 - 실제 객체 사용")
public class LineServiceTest extends ServiceTest {

	@Autowired
	private LineService lineService;
	@Autowired
	private StationService stationService;

	private LineRequest 일호선_요청;
	private LineRequest 오호선_요청;
	private StationResponse 청량리역;
	private StationResponse 종로3가역;
	private StationResponse 서울역;
	private StationResponse 신길역;

	public static void 지하철_노선_목록_포함됨(List<LineResponse> 노선_목록, List<LineResponse> 비교할_노선_목록) {
		List<Long> expectedLineIds = 노선_목록.stream()
			.map(LineResponse::getId)
			.collect(Collectors.toList());

		List<Long> resultLineIds = 비교할_노선_목록.stream()
			.map(LineResponse::getId)
			.collect(Collectors.toList());

		assertThat(resultLineIds).containsAll(expectedLineIds);
	}

	@BeforeEach
	void 초기화() {
		// given
		청량리역 = stationService.saveStation(new StationRequest("청량리역"));
		종로3가역 = stationService.saveStation(new StationRequest("종로3가역"));
		서울역 = stationService.saveStation(new StationRequest("서울역"));
		신길역 = stationService.saveStation(new StationRequest("신길역"));

		일호선_요청 = new LineRequest("1호선", "blue", 종로3가역.getId(), 신길역.getId(), 10);
		오호선_요청 = new LineRequest("5호선", "purple", 종로3가역.getId(), 신길역.getId(), 10);
	}

	@Test
	void 노선_등록() {
		// when
		LineResponse 일호선_응답 = lineService.saveLine(일호선_요청);

		// then
		등록_요청_정보와_응답_정보가_같음(일호선_응답);
	}

	@Test
	void 이미_등록된_노선_등록_요청하는_경우_등록되지_않음() {
		// given
		노선_등록되어_있음(일호선_요청);

		// when

		// then
		등록되지_않음(일호선_요청);
	}

	@Test
	void 노선_목록() {
		// given
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
		LineResponse 일호선_응답 = 노선_등록되어_있음(일호선_요청);

		// when
		LineResponse 조회_노선 = lineService.findLineResponseById(일호선_응답.getId());

		// then
		등록_요청_정보와_응답_정보가_같음(조회_노선);
	}

	@Test
	void 노선_수정() {
		// given
		LineResponse 일호선_응답 = 노선_등록되어_있음(일호선_요청);

		// when
		lineService.updateLine(일호선_응답.getId(), 오호선_요청);

		// then
		LineResponse 조회_노선 = lineService.findLineResponseById(일호선_응답.getId());
		수정_요청_정보와_응답_정보가_같음(조회_노선);
	}

	@Test
	void 노선_삭제() {
		// given
		LineResponse 일호선_응답 = 노선_등록되어_있음(일호선_요청);

		// when
		lineService.deleteLineById(일호선_응답.getId());

		// then
		노선_삭제되었음(일호선_응답);
	}

	@Test
	void 구간_추가() {
		// given
		LineResponse 일호선_응답 = 노선_등록되어_있음(일호선_요청);
		SectionRequest 종로3가역_서울역_구간_요청 = new SectionRequest(종로3가역.getId(), 서울역.getId(), 4);

		// when
		lineService.addLineStation(일호선_응답.getId(), 종로3가역_서울역_구간_요청);

		// then
		LineResponse 조회_노선 = lineService.findLineResponseById(일호선_응답.getId());
		추가_요청_정보와_응답_정보가_같음(조회_노선);
	}

	@Test
	void 구간_추가_동일한_구간_추가한_경우_오류발생() {
		// given
		LineResponse 일호선_응답 = 노선_등록되어_있음(일호선_요청);
		SectionRequest 종로3가역_신길역_구간_요청 = new SectionRequest(종로3가역.getId(), 신길역.getId(), 4);

		// when

		// then
		구간_추가되지_않음(일호선_응답.getId(), 종로3가역_신길역_구간_요청);
	}

	@Test
	void 구간_추가_연관된_역_없이_구간_추가한_경우_오류발생() {
		// given
		LineResponse 일호선_응답 = 노선_등록되어_있음(일호선_요청);
		SectionRequest 청량리역_서울역_구간_요청 = new SectionRequest(청량리역.getId(), 서울역.getId(), 4);

		// when

		// then
		구간_추가되지_않음(일호선_응답.getId(), 청량리역_서울역_구간_요청);
	}

	@Test
	void 구간_삭제() {
		// given
		LineResponse 일호선_응답 = 노선_등록되어_있음(일호선_요청);
		SectionRequest 종로3가역_서울역_구간_요청 = new SectionRequest(종로3가역.getId(), 서울역.getId(), 4);
		lineService.addLineStation(일호선_응답.getId(), 종로3가역_서울역_구간_요청);

		// when
		lineService.removeLineStation(일호선_응답.getId(), 서울역.getId());

		// then
		LineResponse 조회_노선 = lineService.findLineResponseById(일호선_응답.getId());
		삭제된_정보와_응답_정보가_같음(조회_노선);
	}

	private void 등록_요청_정보와_응답_정보가_같음(LineResponse 응답_정보) {
		assertThat(응답_정보.getName()).isEqualTo(일호선_요청.getName());
		assertThat(응답_정보.getColor()).isEqualTo(일호선_요청.getColor());
		assertThat(응답_정보.getStations()).containsSequence(Arrays.asList(종로3가역, 신길역));
	}

	private LineResponse 노선_등록되어_있음(LineRequest 요청) {
		return lineService.saveLine(요청);
	}

	private void 등록되지_않음(LineRequest 일호선_요청) {
		assertThatThrownBy(() -> lineService.saveLine(일호선_요청)).isInstanceOf(RuntimeException.class);
	}

	private void 수정_요청_정보와_응답_정보가_같음(LineResponse 응답_정보) {
		assertThat(응답_정보.getName()).isEqualTo(오호선_요청.getName());
		assertThat(응답_정보.getColor()).isEqualTo(오호선_요청.getColor());
		assertThat(응답_정보.getStations()).containsSequence(Arrays.asList(종로3가역, 신길역));
	}

	private void 노선_삭제되었음(LineResponse 일호선_응답) {
		assertThatThrownBy(() -> lineService.findLineResponseById(일호선_응답.getId())).isInstanceOf(RuntimeException.class);
	}

	private void 추가_요청_정보와_응답_정보가_같음(LineResponse 조회_노선) {
		assertThat(조회_노선.getName()).isEqualTo(일호선_요청.getName());
		assertThat(조회_노선.getColor()).isEqualTo(일호선_요청.getColor());
		assertThat(조회_노선.getStations()).containsSequence(Arrays.asList(종로3가역, 서울역, 신길역));
	}

	private void 구간_추가되지_않음(Long 노선_아이디, SectionRequest 구간_요청) {
		assertThatThrownBy(() -> lineService.addLineStation(노선_아이디, 구간_요청))
			.isInstanceOf(RuntimeException.class);
	}

	private void 삭제된_정보와_응답_정보가_같음(LineResponse 조회_노선) {
		assertThat(조회_노선.getName()).isEqualTo(일호선_요청.getName());
		assertThat(조회_노선.getColor()).isEqualTo(일호선_요청.getColor());
		assertThat(조회_노선.getStations()).containsSequence(Arrays.asList(종로3가역, 신길역));
	}
}
