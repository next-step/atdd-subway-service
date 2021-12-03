package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineFixture;

@DisplayName("경로 파인더")
public class PathFinderTest {

	@DisplayName("경로 파인더 생성")
	@Test
	void of() {
		// given
		List<Line> lines = Arrays.asList(
			LineFixture.신분당선(),
			LineFixture.이호선(),
			LineFixture.삼호선(),
			LineFixture.사호선());

		// when
		PathFinder pathFinder = PathFinder.of(lines);

		// then
		assertThat(pathFinder).isNotNull();
	}
}
