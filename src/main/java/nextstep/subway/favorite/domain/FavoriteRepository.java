package nextstep.subway.favorite.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @EntityGraph(attributePaths = {"sourceStation", "targetStation"})
    List<Favorite> findAllByMemberId(Long memberId);

    @EntityGraph(attributePaths = {"sourceStation", "targetStation"})
    Optional<Favorite> findByMemberIdAndId(Long memberId, Long favoriteId);
}
