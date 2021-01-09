package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class FavoriteServiceTest {

	@Autowired
	private EntityManager em;

	@Autowired
	private FavoriteRepository favoriteRepository;

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private MemberRepository memberRepository;

	private FavoriteService favoriteService;

	private Station 역1;
	private Station 역2;
	private Member 주인Member;
	private Member 다른사람Member;

	private LoginMember 주인;
	private LoginMember 다른사람;

	@BeforeEach
	@Transactional
	void setUp() {
		favoriteService = new FavoriteService(favoriteRepository, stationRepository, memberRepository);
		주인Member = new Member("2mail", "e", 30);
		다른사람Member = new Member("email", "d", 50);
		역1 = new Station("역1");
		역2 = new Station("역2");
		em.persist(주인Member);
		em.persist(다른사람Member);
		em.persist(역1);
		em.persist(역2);
		주인 = new LoginMember(주인Member.getId(), 주인Member.getEmail(), 주인Member.getAge());
		다른사람 = new LoginMember(다른사람Member.getId(), 다른사람Member.getEmail(), 다른사람Member.getAge());
		em.clear();
	}

	@Test
	void getFavorites() {
		// given
		Favorite 즐겨찾기1 = new Favorite(주인Member, 역1, 역2);
		Favorite 즐겨찾기2 = new Favorite(주인Member, 역2, 역1);
		em.persist(즐겨찾기1);
		em.persist(즐겨찾기2);

		// when then
		assertThat(favoriteService.getFavorites(주인))
				.hasSize(2)
				.map(favoriteResponse -> favoriteResponse.getSource().getName())
				.containsExactly(즐겨찾기1.getSource().getName(), 즐겨찾기2.getSource().getName());
		assertThat(favoriteService.getFavorites(다른사람))
				.hasSize(0);
	}

	@Test
	void createFavorite() {
		// given
		FavoriteRequest favoriteRequest = new FavoriteRequest(역1.getId(), 역2.getId());

		// when
		FavoriteResponse favoriteResponse = favoriteService.createFavorite(주인, favoriteRequest);

		// then
		assertThat(favoriteResponse.getId()).isNotNull();
		assertThat(favoriteResponse.getSource().getName()).isEqualTo(역1.getName());
		assertThat(favoriteResponse.getTarget().getName()).isEqualTo(역2.getName());
	}

	@Test
	void createFavorite_역이같은_즐겨찾기_But다른사람() {
		// given: 기존에 다른사람이 역이 같은 즐겨찾기를 등록해놓음
		FavoriteRequest favoriteRequest = new FavoriteRequest(역1.getId(), 역2.getId());
		FavoriteResponse favoriteResponse1 = favoriteService.createFavorite(다른사람, favoriteRequest);

		// when then
		FavoriteResponse favoriteResponse2 = favoriteService.createFavorite(주인, favoriteRequest);
		assertThat(favoriteResponse1.getId()).isNotEqualTo(favoriteResponse2.getId());
	}

	@Test
	void createFavorite_중복됨() {
		// given: 기존에 역이 등록되어 있는 상태
		FavoriteRequest favoriteRequest = new FavoriteRequest(역1.getId(), 역2.getId());
		favoriteService.createFavorite(주인, favoriteRequest);

		// when then
		assertThatThrownBy(() -> favoriteService.createFavorite(주인, favoriteRequest))
				.isInstanceOf(FavoriteValidationException.class)
				.hasMessageContaining("출발역 도착역이 같은 즐겨찾기가 이미 등록되어 있습니다.");
	}

	@Test
	void createFavorite_출발역도착역같음() {
		// given
		FavoriteRequest favoriteRequest = new FavoriteRequest(역1.getId(), 역1.getId());

		// when then
		assertThatThrownBy(() -> favoriteService.createFavorite(주인, favoriteRequest))
				.isInstanceOf(FavoriteValidationException.class)
				.hasMessageContaining("출발역 도착역이 같아 등록이 불가능합니다.");
	}

	@Test
	void createFavorite_미존재역() {
		// given
		FavoriteRequest favoriteRequest = new FavoriteRequest(-1L, -99L);

		// then
		assertThatThrownBy(() -> favoriteService.createFavorite(주인, favoriteRequest))
				.isInstanceOf(FavoriteValidationException.class)
				.hasMessageContaining("존재하지 않는 역을 즐겨찾기로 추가할 수 없습니다.");
	}

	@Test
	void deleteFavoriteById() {
		// given: 기존에 역이 등록되어 있는 상태
		FavoriteRequest favoriteRequest = new FavoriteRequest(역1.getId(), 역2.getId());
		FavoriteResponse favoriteResponse = favoriteService.createFavorite(주인, favoriteRequest);

		// when
		favoriteService.deleteFavoriteById(주인, favoriteResponse.getId());

		// then: 이미 삭제하였으므로 예외발생
		assertThatThrownBy(() -> favoriteService.deleteFavoriteById(주인, favoriteResponse.getId()))
				.isInstanceOf(FavoriteValidationException.class)
				.hasMessageContaining("존재하지 않는 즐겨찾기입니다.");
	}

	@Test
	void deleteFavoriteById_다른사람() {
		// given: 기존에 역이 등록되어 있는 상태
		FavoriteRequest favoriteRequest = new FavoriteRequest(역1.getId(), 역2.getId());
		FavoriteResponse favoriteResponse = favoriteService.createFavorite(주인, favoriteRequest);

		// when then: 다른사람이 주인의 즐겨찾기를 삭제
		assertThatThrownBy(() -> favoriteService.deleteFavoriteById(다른사람, favoriteResponse.getId()))
				.isInstanceOf(FavoriteValidationException.class)
				.hasMessageContaining("존재하지 않는 즐겨찾기입니다.");
	}
}
