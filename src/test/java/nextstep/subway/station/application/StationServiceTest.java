package nextstep.subway.station.application;

import static nextstep.subway.generator.StationGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("역 Service 테스트")
@ExtendWith(MockitoExtension.class)
class StationServiceTest {

	@Mock
	private StationRepository stationRepository;

	@InjectMocks
	private StationService stationService;

	Long 강남역_번호;
	String 강남역_이름;
	Station 강남역;

	StationRequest 강남역_생성_요청;

	@BeforeEach
	void SetUp() {
		초기_역_생성();
	}

	private void 초기_역_생성() {
		강남역_번호 = 1L;
		강남역_이름 = "강남역";
		강남역 = station(강남역_번호, Name.from(강남역_이름));

		강남역_생성_요청 = new StationRequest(강남역_이름);
	}

	@DisplayName("역 생성")
	@Test
	void saveStationTest() {
		// given
		지하철_역_이름이_존재하지_않음(강남역_이름);
		when(stationRepository.save(any(Station.class))).thenReturn(강남역);

		// when
		StationResponse 강남역_생성_응답 = stationService.saveStation(강남역_생성_요청);

		// then
		verify(stationRepository, times(1)).save(any(Station.class));
		assertThat(강남역_생성_응답.getName()).isEqualTo(강남역_이름);
	}

	@DisplayName("이미 존재하는 역 이름으로 역 생성")
	@Test
	void saveStationWithExistingNameTest() {
		// given
		강남역_이름 = "강남역";

		// when
		이미_존재하는_역_이름(강남역_이름);

		// then
		assertThatExceptionOfType(DuplicateDataException.class)
			.isThrownBy(() -> stationService.saveStation(강남역_생성_요청));
	}

	@DisplayName("역 목록 조회")
	@Test
	void findStationTest() {
		// given
		given(stationRepository.findAll()).willReturn(Collections.singletonList(강남역));

		// when
		List<StationResponse> 역_목록_응답 = stationService.findAllStations();

		// then
		assertThat(역_목록_응답)
			.hasSize(1)
			.allSatisfy(it -> assertThat(it.getName()).isEqualTo(강남역_이름));
	}

	@DisplayName("역 조회")
	@Test
	void findStationByIdTest() {
		// given
		given(stationRepository.findById(강남역_번호)).willReturn(Optional.ofNullable(강남역));

		// when
		Station 검색된_역 = stationService.findById(강남역_번호);

		// then
		assertThat(검색된_역.name()).isEqualTo(Name.from(강남역_이름));
	}

	@DisplayName("존재하지 않는 역 조회")
	@Test
	void findStationByIdWithNonExistingStationTest() {
		// given
		given(stationRepository.findById(강남역_번호)).willReturn(Optional.empty());

		// when, then
		assertThatExceptionOfType(NotFoundException.class)
			.isThrownBy(() -> stationService.findById(강남역_번호));
	}

	@DisplayName("역 삭제")
	@Test
	void deleteStationTest() {
		// when
		stationService.deleteStationById(강남역_번호);

		// then
		verify(stationRepository, times(1)).deleteById(강남역_번호);
	}

	private void 이미_존재하는_역_이름(String name) {
		when(stationRepository.existsByName(Name.from(name)))
			.thenReturn(true);
	}

	private void 지하철_역_이름이_존재하지_않음(String name) {
		when(stationRepository.existsByName(Name.from(name)))
			.thenReturn(false);
	}

}