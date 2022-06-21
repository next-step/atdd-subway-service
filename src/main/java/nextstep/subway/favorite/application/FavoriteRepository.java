package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByMemberIdAndUpStationIdAndDownStationId(Long memberId, Long upStationId, Long downStationId);
    boolean existsByIdAndMemberId(Long id, Long memberId);
}
