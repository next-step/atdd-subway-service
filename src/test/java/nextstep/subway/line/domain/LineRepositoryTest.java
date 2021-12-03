package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LineRepositoryTest {

	@Autowired
	private LineRepository lineRepository;

	@DisplayName("노선 ID 리스트에 해당되는 노선 모두 조회")
	@Test
	void findAllByIdExists(){

		Line 신분당선 = new Line("신분당선", "red");
		Line 삼호선 = new Line("삼호선", "orange");
		Line 이호선 = new Line("이호선", "green");
		Line saved_신분당선 = lineRepository.save(신분당선);
		Line saved_삼호선 = lineRepository.save(삼호선);
		Line saved_이호선 = lineRepository.save(이호선);

		List<Line> lines = lineRepository.findAllByLineIds(Arrays.asList(saved_신분당선.getId(),saved_이호선.getId()));

		assertThat(lines.size()).isEqualTo(2);
		assertThat(lines).containsAll(Arrays.asList(신분당선,이호선));
	}
}