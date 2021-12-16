package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.exception.MemberNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exception.StationNotFoundException;

class FavoriteTest {

	@DisplayName("회원이 존재하지 않을 경우 예외발생")
	@Test
	void of_not_found_member() {
		final Station 강남역 = new Station("강남역");
		final Station 선릉역 = new Station("선릉역");
		assertThatExceptionOfType(MemberNotFoundException.class)
			.isThrownBy(() -> Favorite.of(null, 강남역, 선릉역));
	}

	@DisplayName("출발역이 존재하지 않을 경우 예외발생")
	@Test
	void of_not_found_source_station() {
		final Member member = new Member("member@email.com", "<secret>", 20);
		final Station 서현역 = new Station("서현역");
		assertThatExceptionOfType(StationNotFoundException.class)
			.isThrownBy(() -> Favorite.of(member, null, 서현역));
	}

	@DisplayName("도착역이 존재하지 않을 경우 예외발생")
	@Test
	void of_not_found_target_station() {
		final Member member = new Member("member@email.com", "<secret>", 20);
		final Station 신사역 = new Station("신사역");
		assertThatExceptionOfType(StationNotFoundException.class)
			.isThrownBy(() -> Favorite.of(member, 신사역, null));
	}

	@DisplayName("출발역과 도착역이 동일한 경우 예외발생")
	@Test
	void of_same_stations() {
		final Member member = new Member("member@email.com", "<secret>", 20);
		final Station 강남역 = new Station("강남역");
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> Favorite.of(member, 강남역, 강남역));
	}
}
