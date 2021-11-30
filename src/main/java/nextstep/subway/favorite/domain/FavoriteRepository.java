package nextstep.subway.favorite.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @EntityGraph(attributePaths = {"source", "target"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Favorite> findAllByMemberId(Long memberId);

    @EntityGraph(attributePaths = {"source", "target"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Favorite> findById(Long id);
}
