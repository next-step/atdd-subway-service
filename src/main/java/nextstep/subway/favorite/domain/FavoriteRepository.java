package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByMemberIdAndSourceStationAndTargetStation(Long memberId, Station sourceStation, Station targetStation);
}
