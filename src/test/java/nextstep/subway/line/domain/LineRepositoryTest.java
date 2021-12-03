package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@DataJpaTest
class LineRepositoryTest {

	@Autowired
	private LineRepository lineRepository;

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private SectionRepository sectionRepository;

	private Station 신논현;
	private Station 양재역;
	private Station 강남역;
	private Station 역삼역;

	private Line 신분당선;
	private Line 삼호선 ;
	private Line 이호선 ;

	@BeforeEach
	void setUp(){
		신논현 = new Station("신논현");
		양재역 = new Station("양재역");
		강남역 = new Station("강남역");
		역삼역 = new Station("역삼역");
		stationRepository.save(신논현);
		stationRepository.save(양재역);
		stationRepository.save(강남역);
		stationRepository.save(역삼역);

		신분당선 = new Line("신분당선", "red");
		삼호선 = new Line("삼호선", "orange");
		이호선 = new Line("이호선", "green");
		lineRepository.save(신분당선);
		lineRepository.save(삼호선);
		lineRepository.save(이호선);
	}

	@DisplayName("두 지하철역이 포함된 지하철 노선 모두 조회")
	@Test
	void findAllContainsStations(){

		// given
		Section section1 = new Section(신분당선,신논현,양재역,5 );
		Section section2 = new Section(신분당선,역삼역,강남역,5 );
		Section section3 = new Section(삼호선,양재역,역삼역,5 );
		sectionRepository.save(section1);
		sectionRepository.save(section2);
		sectionRepository.save(section3);

		List<Line> lines = lineRepository.findAllExistStations(Arrays.asList(신논현.getId(),강남역.getId()));
		System.out.println(lines);
		assertThat(lines.size()).isEqualTo(1);
		assertThat(lines).containsAll(Arrays.asList(신분당선));
	}
}