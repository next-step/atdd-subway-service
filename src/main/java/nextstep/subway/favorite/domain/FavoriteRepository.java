package nextstep.subway.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("select f from Favorite f join fetch f.source join fetch f.target where f.memberId = :memberId")
    List<Favorite> findByMemberId(@Param("memberId") Long memberId);
}
