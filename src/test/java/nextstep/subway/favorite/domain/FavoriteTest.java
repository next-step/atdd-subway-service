package nextstep.subway.favorite.domain;

import static nextstep.subway.generator.StationGenerator.*;
import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.Favorite.domain.Favorite;
import nextstep.subway.common.domain.Name;
import nextstep.subway.station.domain.Station;

@DisplayName("즐겨찾기 테스트")
class FavoriteTest {

	@DisplayName("즐겨찾기 생성")
	@Test
	void createFavoriteTest() {
		assertThatNoException()
			.isThrownBy(() -> Favorite.of(station(Name.from("강남역")), station(Name.from("역삼역")), 1L));
	}

	@DisplayName("출발역 혹은 도착역이 없으면 예외 발생")
	@ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 즐겨찾기 생성 불가능")
	@MethodSource
	void createFavoriteWithoutStationTest(Station source, Station target) {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Favorite.of(source, target, 1L))
			.withMessageEndingWith("필수입니다.");
	}

	@DisplayName("출발역과 도착역이 같으면 예외 발생")
	@Test
	void createFavoriteWithSameStationTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Favorite.of(station(Name.from("강남역")), station(Name.from("강남역")), 1L))
			.withMessageEndingWith("같을 수 없습니다.");
	}

	private static Stream<Arguments> createFavoriteWithoutStationTest() {
		return Stream.of(
			Arguments.of(station(Name.from("강남역")), null),
			Arguments.of(null, station(Name.from("역삼역")))
		);
	}
}