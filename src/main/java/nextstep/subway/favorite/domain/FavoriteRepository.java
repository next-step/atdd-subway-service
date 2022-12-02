package nextstep.subway.favorite.domain;

import java.util.List;
import java.util.Optional;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByMemberId(Long loginMemberId);

    Optional<Favorite> findBySourceAndTargetAndMember(Station source, Station target, Member member);
}
