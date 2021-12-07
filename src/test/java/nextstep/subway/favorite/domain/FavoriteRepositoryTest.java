package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@DataJpaTest
class FavoriteRepositoryTest {

	@Autowired
	private FavoriteRepository favoriteRepository;

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private MemberRepository memberRepository;

	private Member member;
	private Station source;
	private Station target;

	@BeforeEach
	void setUp() {
		// given
		member = new Member("email@email.com", "password", 30);
		source = new Station("강남역");
		target = new Station("광교역");

		memberRepository.save(member);
		stationRepository.save(source);
		stationRepository.save(target);
	}

	@DisplayName("즐겨찾기 등록")
	@Test
	public void saveFavorite() {
		// when
		Favorite favorite = new Favorite(member, source, target);
		Favorite savedFavorite = favoriteRepository.save(favorite);

		// then
		assertThat(favorite).isEqualTo(savedFavorite);
	}

	@DisplayName("즐겨찾기 삭제")
	@Test
	public void deleteFavorite() {
		// given
		Favorite savedFavorite = favoriteRepository.save(new Favorite(member, source, target));

		// when
		favoriteRepository.delete(savedFavorite);

		// then
		Optional<Favorite> findDeletedFavorite = favoriteRepository.findById(savedFavorite.getId());
		assertThat(findDeletedFavorite.isPresent()).isFalse();
	}

	@DisplayName("즐겨찾기 목록 조회")
	@Test
	public void findFavoriteByMember() {
		// given
		Member otherUser = memberRepository.save(new Member("other@email.com", "password", 25));

		Favorite savedFavorite = favoriteRepository.save(new Favorite(member, source, target));
		favoriteRepository.save(new Favorite(otherUser, source, target));

		// when
		List<Favorite> favorites = favoriteRepository.findFavoriteByMember(member);

		// then
		assertThat(favorites.size()).isEqualTo(1);
		assertThat(favorites.get(0)).isEqualTo(savedFavorite);
	}

	@DisplayName("즐겨찾기 목록 조회(Station 함께 가져오기)")
	@Test
	public void findWithStationsByMember() {
		// given
		Member otherUser = memberRepository.save(new Member("other@email.com", "password", 25));

		Favorite savedFavorite = favoriteRepository.save(new Favorite(member, source, target));
		favoriteRepository.save(new Favorite(otherUser, source, target));

		// when
		List<Favorite> favorites = favoriteRepository.findWithStationsByMember(member.getId());

		// then
		assertThat(favorites.size()).isEqualTo(1);
		assertThat(favorites.get(0)).isEqualTo(savedFavorite);
	}
}