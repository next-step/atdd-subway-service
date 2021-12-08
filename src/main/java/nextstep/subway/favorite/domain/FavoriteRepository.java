package nextstep.subway.favorite.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nextstep.subway.member.domain.Member;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	List<Favorite> findFavoriteByMember(Member member);

	@Query("select f, source, target from Favorite f " +
	"left outer join Station source on f.source.id=source.id "+
	"left outer join Station target on f.target.id=target.id "+
	"where f.member.id= :memberId")
	List<Favorite> findWithStationsByMember(@Param("memberId") Long memberId);
}
