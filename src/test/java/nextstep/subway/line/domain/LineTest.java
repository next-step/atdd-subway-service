package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@DisplayName("Line의 Domain 단위테스트")
@DataJpaTest
public class LineTest {

	@Autowired
	LineRepository lineRepository;
	@Autowired
	StationRepository stationRepository;

	private Station 교대역;
	private Station 강남역;
	private Station 역삼역;
	private Station 선릉역;
	private Station 삼성역;
	private Station 잠실역;
	private Station 천호역;
	private Station 군자역;
	private Line 이호선;
	private Line 오호선;

	@BeforeEach
	void addSection() {
		교대역 = stationRepository.save(new Station("교대역"));
		강남역 = stationRepository.save(new Station("강남역"));
		역삼역 = stationRepository.save(new Station("역삼역"));
		선릉역 = stationRepository.save(new Station("선릉역"));
		삼성역 = stationRepository.save(new Station("삼성역"));
		잠실역 = stationRepository.save(new Station("잠실역"));
		천호역 = stationRepository.save(new Station("천호역"));
		군자역 = stationRepository.save(new Station("군자역"));

		이호선 = lineRepository.save(new Line("2호선", "green", 100));
		오호선 = lineRepository.save(new Line("5호선", "purple", 200));

		이호선.addSection(교대역, 삼성역,10);
		이호선.addSection(선릉역, 삼성역, 5);
		이호선.addSection(삼성역, 잠실역, 7);
	}

	@Test
	void getStations() {
		assertThat(이호선.getStations()).containsExactly(교대역, 선릉역, 삼성역, 잠실역);
		assertThat(이호선.getExtraFare()).isEqualTo(100);
	}

	@Test
	void removeSection() {
		이호선.removeStation(선릉역);
		assertThat(이호선.getStations()).containsExactly(교대역, 삼성역, 잠실역);
	}
}
