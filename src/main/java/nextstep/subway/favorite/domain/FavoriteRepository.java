package nextstep.subway.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByMemberId(Long id);

    Optional<Favorite> findByIdAndMemberId(Long favoriteId, Long id);
}
