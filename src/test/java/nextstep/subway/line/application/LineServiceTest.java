package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("노선 서비스 테스트 - 실제 객체 사용")
@SpringBootTest
public class LineServiceTest {

	@Autowired
	private LineService lineService;
	@Autowired
	private StationService stationService;

	private LineRequest 일호선_요청;
	private StationResponse 종로3가역;
	private StationResponse 신길역;

	@BeforeEach
	void 초기화() {
		// given
		종로3가역 = stationService.saveStation(new StationRequest("종로3가역"));
		신길역 = stationService.saveStation(new StationRequest("신길역"));

		일호선_요청 = new LineRequest("1호선", "blue", 종로3가역.getId(), 신길역.getId(), 10);
	}

	@Test
	void 노선_등록() {
		// when
		LineResponse 일호선_응답 = lineService.saveLine(일호선_요청);

		// then
		등록_요청_정보와_응답_정보가_같음(일호선_응답);
	}

	private void 등록_요청_정보와_응답_정보가_같음(LineResponse 응답_정보) {
		assertThat(응답_정보.getName()).isEqualTo(일호선_요청.getName());
		assertThat(응답_정보.getColor()).isEqualTo(일호선_요청.getColor());
		assertThat(응답_정보.getStations()).containsSequence(Arrays.asList(종로3가역, 신길역));
	}
}
