package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@DisplayName("Favorite의 Domain 단위테스트")
@DataJpaTest
public class FavoriteTest {

	@Autowired
	FavoriteRepository favoriteRepository;
	@Autowired
	StationRepository stationRepository;
	@Autowired
	MemberRepository memberRepository;

	private Station 서울역;
	private Station 강남역;
	private Station 천호역;
	private Member 사용자;
	private Member 다른_사용자;

	@BeforeEach
	void setUp() {
		서울역 = stationRepository.save(new Station("서울역"));
		강남역 = stationRepository.save(new Station("강남역"));
		천호역 = stationRepository.save(new Station("천호역"));
		사용자 = memberRepository.save(new Member("email@email.com", "password", 20));
		다른_사용자 = memberRepository.save(new Member("other@email.com", "password", 21));
	}

	@Test
	void saveFavorite() {
		// when
		Favorite savedFavorite = favoriteRepository.save(new Favorite(사용자, 서울역, 강남역));

		// then
		assertThat(savedFavorite.getId()).isNotZero();
		assertThat(savedFavorite.getMember()).isEqualTo(사용자);
		assertThat(savedFavorite.getSource()).isEqualTo(서울역);
		assertThat(savedFavorite.getTarget()).isEqualTo(강남역);
	}

	@DisplayName("자신의 즐겨찾기 목록 조회")
	@Test
	void findFavorites() {
		// given
		favoriteRepository.save(new Favorite(사용자, 서울역, 강남역));
		favoriteRepository.save(new Favorite(사용자, 강남역, 천호역));

		// when
		List<Favorite> favorites = favoriteRepository.findAllByMemberId(사용자.getId());

		// then
		assertThat(favorites)
			.anySatisfy(favorite -> {
				assertThat(favorite.getId()).isNotZero();
				assertThat(favorite.getMember()).isEqualTo(사용자);
				assertThat(favorite.getSource()).isEqualTo(서울역);
				assertThat(favorite.getTarget()).isEqualTo(강남역);
			})
			.anySatisfy(favorite -> {
				assertThat(favorite.getId()).isNotZero();
				assertThat(favorite.getMember()).isEqualTo(사용자);
				assertThat(favorite.getSource()).isEqualTo(강남역);
				assertThat(favorite.getTarget()).isEqualTo(천호역);
			});
	}
}
