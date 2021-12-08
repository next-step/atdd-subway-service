package nextstep.subway.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * packageName : nextstep.subway.favorites.domain
 * fileName : FavoriteRepository
 * author : haedoang
 * date : 2021/12/08
 * description :
 */
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query(value = "select f from Favorite f where f.sourceStation.id =?1 and f.targetStation.id =?2 and f.member.id = ?3")
    Favorite findFavorite(Long source, Long target, Long memberId);
}