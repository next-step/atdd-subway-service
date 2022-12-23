package nextstep.subway.favorite.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByMemberId(Long memberId);
    Optional<Favorite> findByIdAndMemberId(Long favoriteId, Long memberId);
}
