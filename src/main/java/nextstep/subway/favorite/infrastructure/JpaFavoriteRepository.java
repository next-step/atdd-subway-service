package nextstep.subway.favorite.infrastructure;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepository {
}
