package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@DataJpaTest
public class SectionsTest {
	@Autowired
	LineRepository lineRepository;

	@Autowired
	StationRepository stationRepository;

	Line line;
	Station 교대역;
	Station 강남역;
	Station 선릉역;
	Station 삼성역;

	@BeforeEach
	public void setUp() {
		교대역 = stationRepository.save(new Station("교대역"));
		강남역 = stationRepository.save(new Station("강남역"));
		선릉역 = stationRepository.save(new Station("선릉역"));
		삼성역 = stationRepository.save(new Station("삼성역"));
		line = new Line("2호선", "green", 교대역, 삼성역, 10);
		lineRepository.save(line);
	}

	@Test
	public void getStations() {
		int expected = 2;
		List<Station> stations = line.getStations();
		assertThat(expected).isEqualTo(stations.size());
	}

	@Test
	public void addLineStation() {
		int expected = 3;
		line.addLineStation(교대역, 강남역, 5);
		List<Station> stations = line.getStations();
		assertThat(expected).isEqualTo(stations.size());
	}

	@Test
	public void removeLineStation() {
		int expected = 2;
		line.addLineStation(교대역, 강남역, 5);
		line.removeLineStation(강남역);
		List<Station> stations = line.getStations();
		assertThat(expected).isEqualTo(stations.size());
	}
}
