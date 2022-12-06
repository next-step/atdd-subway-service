package nextstep.subway.favorite.repository;

import nextstep.subway.favorite.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByMemberId(Long memberId);
    Optional<Favorite> findByIdAndMemberId(Long favoriteId, Long memberId);
}
