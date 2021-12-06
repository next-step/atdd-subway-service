package nextstep.subway.favorites.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName : nextstep.subway.favorites.domain
 * fileName : FavoriteRepository
 * author : haedoang
 * date : 2021/12/07
 * description :
 */
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}
