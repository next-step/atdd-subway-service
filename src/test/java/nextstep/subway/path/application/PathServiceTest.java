package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.NothingException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

@DisplayName("First Outside In Test")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

	@Mock
	private LineService lineService;

	private PathService pathService;

	private Line line1;
	private Line line2;
	private Station 시청역;
	private Station 서초역;
	private Station 인천역;
	private Station 주안역;

	private LoginMember loginMember;

	@BeforeEach
	void setUp() {
		pathService = new PathService(lineService);

		시청역 = new Station(1L, "시청역");
		서초역 = new Station(2L, "서초역");
		인천역 = new Station(3L, "인천역");
		주안역 = new Station(4L, "주안역");

		line1 = new Line(1L, "1호선", "blue", 인천역, 주안역, 50, 0);
		line2 = new Line(2L, "2호선", "green", 시청역, 서초역, 100, 0);

		loginMember = new LoginMember();
	}

	@DisplayName("경로찾기: [에러]동일한 역 경로검색")
	@Test
	void foundPathSameStationTest() {
		// given
		given(lineService.findStationById(1L)).willReturn(시청역);

		// when 
		PathRequest request = new PathRequest(시청역.getId(), 시청역.getId());

		// then
		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> pathService.findPath(loginMember, request));
	}

	@DisplayName("경로찾기: [에러]존재하지 않는 역 경로검색")
	@Test
	void notFoundStationTest() {
		// given
		given(lineService.findStationById(1L)).willReturn(시청역);
		given(lineService.findStationById(3L)).willThrow(NothingException.class);

		// when 
		PathRequest request = new PathRequest(시청역.getId(), 3L);

		// then
		assertThatExceptionOfType(NothingException.class)
			.isThrownBy(() -> pathService.findPath(loginMember, request));
	}

	@DisplayName("경로찾기: [에러]같은 경로에 없는 역 경로검색")
	@Test
	void notFoundPathTest() {
		// given
		given(lineService.findLineAll()).willReturn(Arrays.asList(line1, line2));
		given(lineService.findStationById(1L)).willReturn(시청역);
		given(lineService.findStationById(3L)).willReturn(인천역);

		// when
		PathRequest request = new PathRequest(시청역.getId(), 인천역.getId());

		// then
		assertThatExceptionOfType(NothingException.class)
			.isThrownBy(() -> pathService.findPath(loginMember, request));
	}

	@DisplayName("경로찾기: 최단거리를 검색한다.")
	@Test
	void findPathTest() {
		// given
		given(lineService.findLineAll()).willReturn(Arrays.asList(line1, line2));
		given(lineService.findStationById(3L)).willReturn(인천역);
		given(lineService.findStationById(4L)).willReturn(주안역);
		PathRequest request = new PathRequest(인천역.getId(), 주안역.getId());

		// when
		PathResponse finder = pathService.findPath(loginMember, request);

		// then
		assertAll(
			() -> assertThat(finder.getStations()).isNotNull(),
			() -> assertThat(finder.getStations()).hasSize(2),
			() -> assertThat(finder.getDistance()).isEqualTo(50)
		);
	}
}
