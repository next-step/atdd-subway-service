package nextstep.subway.favorites.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

    @EntityGraph(attributePaths = {"source", "target"})
    List<Favorites> findAllByMemberId(Long memberId);

    boolean existsBySourceIdAndTargetId(Long id, Long memberId);

    Optional<Favorites> findByIdAndMemberId(Long id, Long memberId);
}
