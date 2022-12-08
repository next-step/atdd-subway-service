package nextstep.subway.favorite.domain;

import java.util.List;
import java.util.Optional;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @EntityGraph(attributePaths = {"source", "target"})
    @Query(value = "select DISTINCT f from Favorite f where f.member.id = :memberId")
    List<Favorite> findAllByMemberId(Long memberId);

    Optional<Favorite> findBySourceAndTargetAndMember(Station source, Station target, Member member);

    Optional<Favorite> findByIdAndMemberId(Long id, Long loginMemberId);
}
