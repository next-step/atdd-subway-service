package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.util.Lists;
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
	private MemberRepository memberRepository;

	@Autowired
	private StationRepository stationRepository;

	private static Member 사용자 = new Member("email@naver.com", "password", 33);
	private static Station 두정역 = new Station("두정역");
	private static Station 천안역 = new Station("천안역");

	private Favorite saveFavorite;
	private Member saveMember;
	private Station saveSource;
	private Station saveTarget;

	@BeforeEach
	public void setUp() {
		saveMember = memberRepository.save(사용자);
		saveSource = stationRepository.save(두정역);
		saveTarget = stationRepository.save(천안역);
		saveFavorite = favoriteRepository.save(new Favorite(saveMember, saveSource, saveTarget));
	}

	@Test
	@DisplayName("즐겨찾기 생성 테스트")
	public void create() {
		assertThat(saveFavorite).isEqualTo(new Favorite(1L, 사용자, 두정역, 천안역));
	}

	@Test
	@DisplayName("즐겨찾기 사용자 별 목록 조회 테스트")
	public void findAll() {
		//when
		List<Favorite> favorites = favoriteRepository.findFavoritesByMember(saveMember);

		//then
		assertThat(favorites).hasSize(1);
	}

	@Test
	@DisplayName("즐겨찾기 삭제 테스트")
	public void delete() {
		//when
		favoriteRepository.delete(saveFavorite);
		List<Favorite> favorites = favoriteRepository.findFavoritesByMember(saveMember);

		//then
		assertThat(favorites).hasSize(0);
	}

}
