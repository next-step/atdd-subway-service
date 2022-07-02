package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByMemberIdAndSourceAndTarget(Long memberId, Station source, Station target);

    List<Favorite> findAllByMemberId(Long memberId);
}
