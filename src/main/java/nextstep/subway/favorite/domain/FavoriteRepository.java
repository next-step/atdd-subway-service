package nextstep.subway.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : byungkyu
 * @date : 2021/01/18
 * @description :
 **/
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}
