package nextstep.subway.line.application;

import static nextstep.subway.generator.SectionGenerator.*;
import static nextstep.subway.generator.StationGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.line.domain.Color;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.line.dto.SectionRequest;
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

	@Mock
	private SectionService sectionService;

	@InjectMocks
	private LineService lineService;

	private LineCreateRequest 이호선_생성_요청;
	private Line 이호선;
	private Station 강남역;
	private Station 역삼역;
	private Station 선릉역;
	private Long 강남역_번호;
	private Long 역삼역_번호;
	private Long 선릉역_번호;

	private Section 강남역_역삼역_구간;
	private Section 역삼역_선릉역_구간;

	@BeforeEach
	void setUp() {
		초기_노선_생성();
	}

	private void 초기_노선_생성() {
		강남역_번호 = 1L;
		역삼역_번호 = 2L;
		선릉역_번호 = 3L;
		강남역 = station(강남역_번호, Name.from("강남역"));
		역삼역 = station(역삼역_번호, Name.from("역삼역"));
		선릉역 = station(선릉역_번호, Name.from("선릉역"));

		이호선_생성_요청 = new LineCreateRequest("이호선", "green", 강남역_번호, 역삼역_번호, 10);

		강남역_역삼역_구간 = Section.of(강남역, 역삼역, Distance.from(10));
		이호선 = Line.of(Name.from("이호선"), Color.from("green"),
			Sections.from(강남역_역삼역_구간));
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
		verify(stationService, times(2)).findById(anyLong());
		verify(lineRepository, times(1)).save(any(Line.class));

		assertThat(노선에_속한_역_목록(이호선_생성_응답)).containsExactly("강남역", "역삼역");
	}

	@Test
	@DisplayName("노선 저장 - 존재하지 않는 역이면 예외")
	void saveLineWithNotExistingStationTest() {
		// given
		given(stationService.findById(anyLong())).willThrow(NotFoundException.class);

		// when
		assertThatExceptionOfType(NotFoundException.class)
			.isThrownBy(() -> lineService.saveLine(이호선_생성_요청));

		// then
		verify(stationService, times(1)).findById(anyLong());
	}

	@Test
	@DisplayName("노선 저장 - 이미 노선에 등록되어 있는 역이면 예외")
	void saveLineWithAlreadyExistingStationTest() {
		// given
		String name = "이호선";

		// when
		이미_존재하는_노선_이름(name);

		// then
		assertThatExceptionOfType(DuplicateDataException.class)
			.isThrownBy(() -> lineService.saveLine(이호선_생성_요청));
	}

	@Test
	@DisplayName("노선 목록 조회")
	void findLinesTest() {
		// given
		given(lineRepository.findAll()).willReturn(Collections.singletonList(이호선));

		// when
		List<LineResponse> 노선_목록_응답 = lineService.findLines();

		// then
		verify(lineRepository, times(1)).findAll();

		assertThat(노선_목록_응답)
			.hasSize(1)
			.allSatisfy(it -> assertThat(it.getName()).isEqualTo(이호선.getName()));
		assertThat(노선에_속한_역_목록(노선_목록_응답.get(0))).containsExactly("강남역", "역삼역");
	}

	@Test
	@DisplayName("노선 조회")
	void findLineTest() {
		// given
		검색된_노선(이호선);

		// when
		LineResponse 노선_응답 = lineService.findLineResponseById(1L);

		// then
		verify(lineRepository, times(1)).findById(anyLong());

		assertThat(노선_응답.getName()).isEqualTo(이호선.getName());
		assertThat(노선에_속한_역_목록(노선_응답)).containsExactly("강남역", "역삼역");
	}

	@Test
	@DisplayName("노선 수정")
	void updateLineTest() {
		// given
		String 이호선_수정_이름 = "신분당선";
		String 이호선_수정_색상 = "red";
		LineUpdateRequest 이호선_수정_요청 = new LineUpdateRequest(이호선_수정_이름, 이호선_수정_색상);
		// 저장된_노선(이호선);
		검색된_노선(이호선);

		// when
		lineService.updateLine(anyLong(), 이호선_수정_요청);

		// then
		verify(lineRepository).findById(anyLong());
		assertThat(이호선.getName()).isEqualTo(이호선_수정_이름);
		assertThat(이호선.getColor()).isEqualTo(이호선_수정_색상);
	}

	@Test
	@DisplayName("노선 수정 - 이미 존재하는 노선 이름이면 예외")
	void updateLineWithExistingNameTest() {
		// given
		String 이호선_수정_이름 = "신분당선";
		String 이호선_수정_색상 = "red";
		이미_존재하는_노선_이름(이호선_수정_이름);

		// when
		LineUpdateRequest 이호선_수정_요청 = new LineUpdateRequest(이호선_수정_이름, 이호선_수정_색상);

		// then
		assertThatExceptionOfType(DuplicateDataException.class)
			.isThrownBy(() -> lineService.updateLine(Long.MAX_VALUE, 이호선_수정_요청));
	}

	@Test
	@DisplayName("구간 추가")
	void addSectionTest() {
		// given
		검색된_노선(이호선);
		검색된_지하철역(강남역_번호, 강남역);
		검색된_지하철역(선릉역_번호, 선릉역);

		// when
		when(sectionService.findSectionsToUpdate(강남역, 선릉역, 이호선)).thenReturn(Collections.emptyList());
		lineService.addLineStation(1L, new SectionRequest(강남역_번호, 선릉역_번호, 10));

		// then
		assertThat(이호선.getSections()).hasSize(2);
	}

	@Test
	@DisplayName("구간 삭제")
	void removeSectionTest() {
		// given
		Section 역삼역_선릉역_구간 = section("강남역", "선릉역", 5);
		이호선.connectSection(역삼역_선릉역_구간, Collections.singletonList(강남역_역삼역_구간));

		// when
		when(sectionService.findSectionByUpStation(강남역_번호)).thenReturn(강남역_역삼역_구간);
		when(sectionService.findSectionByDownStation(강남역_번호)).thenReturn(강남역_역삼역_구간);
		when(lineRepository.findById(any())).thenReturn(Optional.ofNullable(이호선));

		lineService.removeLineStation(이호선.getId(), 강남역_번호);

		// then
		assertThat(이호선.getSections()).hasSize(1);
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
		when(stationService.findById(id)).thenReturn(station);
	}

	private void 검색된_노선(Line line) {
		given(lineRepository.findById(anyLong())).willReturn(Optional.of(line));
	}

	private void 생성된_노선() {
		when(lineRepository.save(any(Line.class))).then(AdditionalAnswers.returnsFirstArg());
	}

	private void 이미_존재하는_노선_이름(String name) {
		when(lineRepository.existsByName(Name.from(name))).thenReturn(true);
	}
}