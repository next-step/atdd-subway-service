package nextstep.subway.line.acceptance;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("단위 테스트")
public class LineTest {
	Station upStation;

	Station downStation;

	Line line;

	@BeforeEach
	public void setUp() {
		upStation = new Station("강남역");
		downStation = new Station("광교역");
		line = new Line("신분당선", "RED", upStation, downStation, 10);
	}


	@Test
	@DisplayName("역 응답 모델을 조회한다.")
	void getStationResponses() {
		List<StationResponse> responses = line.getStationResponses();
		assertThat(responses.size()).isEqualTo(2);
		assertThat(responses.stream().map(StationResponse::getName).collect(Collectors.toList())).containsExactly(upStation.getName(), downStation.getName());
	}

	@Test
	@DisplayName("역 도메인을 조회한다.")
	void getStations() {
		List<Station> responses = line.getStations();
		assertThat(responses.size()).isEqualTo(2);
		assertThat(responses.stream().map(Station::getName).collect(Collectors.toList())).containsExactly(upStation.getName(), downStation.getName());
	}


	@Test
	@DisplayName("상행역을 찾는다.")
	void findUpStation() {
		Station station = line.findUpStation();
		assertThat(station.getName()).isEqualTo(upStation.getName());
	}

	@Test
	@DisplayName("노선에 새 구간을 추가한다.")
	void addLineStation() {
		Station newUpStation = new Station("청계산입구역");
		line.addLineStation(newUpStation, downStation, 4);

		assertThat(line.getStations().size()).isEqualTo(3);
		assertThat(line.getStations().stream().map(Station::getName).collect(Collectors.toList())).containsExactly(upStation.getName(), newUpStation.getName(), downStation.getName());
	}

	@Test
	@DisplayName("노선 업데이트 유효성을 체크한다. 추가되는 구간은 기존 구간의 길이보다 작아야한다는 조건 확인")
	void validate() {
		assertThatThrownBy(() -> line.validate(upStation, downStation, 11)).isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("노선 업데이트 유효성을 체크한다. 구간은 최소 1개 이상이어야한다는 조건 확인")
	void validateMinSize() {
		assertThatThrownBy(() -> line.validateMinSize()).isInstanceOf(RuntimeException.class);
	}
}

