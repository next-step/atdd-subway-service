package nextstep.subway.favorite.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.subway.member.domain.Member;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	List<Favorite> findFavoritesByMember(Member member);
}
