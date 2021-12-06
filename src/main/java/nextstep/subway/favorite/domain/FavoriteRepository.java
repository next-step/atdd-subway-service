package nextstep.subway.favorite.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("select f from Favorite f "
        + "join fetch f.member m "
        + "join fetch f.source s "
        + "join fetch f.target t "
        + "where m.id=:memberId")
    List<Favorite> findFavoritesById(@Param("memberId") Long memberId);
}
