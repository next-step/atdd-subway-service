package nextstep.subway.favorite.domain;

import java.util.List;
import java.util.Optional;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query(value = "select f from Favorite f "
        + "join fetch f.source "
        + "join fetch f.target "
        + "where f.member = :member")
    List<Favorite> findAllByMember(Member member);

    Optional<Favorite> findBySourceAndTargetAndMember(Station source, Station target, Member member);
}
