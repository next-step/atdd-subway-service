package nextstep.subway.favorite.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @EntityGraph(attributePaths = {"sourceStation", "targetStation"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Favorite> findByMemberId(Long memberId);
}
