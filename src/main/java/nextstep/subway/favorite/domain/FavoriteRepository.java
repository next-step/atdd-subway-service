package nextstep.subway.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByMemberId(Long memberId);

    boolean existsBySourceIdAndTargetId(Long sourceId, Long targetId);

    Optional<Favorite> findByIdAndMemberId(Long Id, Long memberId);

}
