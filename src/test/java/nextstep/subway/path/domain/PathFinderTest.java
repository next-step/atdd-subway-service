package nextstep.subway.path.domain;

import static nextstep.subway.line.domain.LineFixture.*;
import static nextstep.subway.line.domain.SectionFixture.*;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

@DisplayName("경로 파인더")
public class PathFinderTest {

	@DisplayName("경로 파인더 생성")
	@Test
	void of() {
		// given
		List<Line> lines = Arrays.asList(
			신분당선(),
			이호선(),
			삼호선(),
			사호선());

		// when
		PathFinder pathFinder = PathFinder.of(lines);

		// then
		assertThat(pathFinder).isNotNull();
	}

	@DisplayName("경로 찾기")
	@Test
	void find() {
		// given
		PathFinder pathFinder = PathFinder.of(Arrays.asList(
			신분당선(),
			이호선(),
			삼호선(),
			사호선()));

		// when
		Path path = pathFinder.find(양재시민의숲역(), 선릉역());

		// then
		assertAll(
			() -> assertThat(path).isNotNull(),
			() -> assertThat(path.getStations()).isEqualTo(Arrays.asList(
				양재시민의숲역(),
				양재역(),
				강남역(),
				역삼역(),
				선릉역())),
			() -> assertThat(path.getDistance()).isEqualTo(Stream.of(
					양재역_양재시민의숲역_구간(),
					강남역_양재역_구간(),
					강남역_역삼역_구간(),
					역삼역_선릉역_구간())
				.map(Section::getDistance)
				.reduce(Integer::sum)
				.get()));
	}
}
