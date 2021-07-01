package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
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
	private Line 일호선;
	private LineRequest 일호선_요청;

	@BeforeEach
	void 초기화() {
		종로3가역 = new Station("종로3가역");
		신길역 = new Station("신길역");
		일호선 = new Line("1호선", "blue", 종로3가역, 신길역, 10);
		일호선_요청 = new LineRequest("1호선", "blue", 1L, 2L, 10);
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
}
