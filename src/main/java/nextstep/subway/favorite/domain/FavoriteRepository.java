package nextstep.subway.favorite.domain;

import java.util.List;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findFavoritesByMember(Member member);

    boolean existsByMemberAndSourceStationAndTargetStation(Member member, Station sourceStation, Station targetStation);
}
