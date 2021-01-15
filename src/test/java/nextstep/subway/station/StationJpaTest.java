package nextstep.subway.station;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
public class StationJpaTest {

	@Autowired
	private StationRepository stationRepository;

	@Test
	void test(){
		Station station1 = stationRepository.save(new Station("강변역"));
		Station station2 = stationRepository.save(new Station("강남역"));

		List<Station> stations = stationRepository.findAllByIdIn(Arrays.asList(station1.getId(), station2.getId()));
		for(Station s : stations){
			System.out.println(s);
		}
	}
}
