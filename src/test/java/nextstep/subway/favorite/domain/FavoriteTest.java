package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@DataJpaTest
class FavoriteTest {

	@Autowired
	private EntityManager em;

	private Member member;
	private Station source;
	private Station target;

	@BeforeEach
	void setUp() {
		// given
		member = new Member("email@email.com", "password", 30);
		source = new Station("강남역");
		target = new Station("광교역");

		em.persist(member);
		em.persist(source);
		em.persist(target);
	}

	@DisplayName("Favorite 생성")
	@Test
	public void constructFavorite() {
		// when
		Favorite favorite = new Favorite(member, source, target);
		em.persist(favorite);

		// then
		Favorite findFavorite = em.find(Favorite.class, favorite.getId());
		assertThat(favorite).isEqualTo(findFavorite);
	}
}