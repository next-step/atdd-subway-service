package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    //    @EntityGraph(attributePaths = {"source", "target"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select f from Favorite f join fetch f.member join f.source join f.target where f.member.id = :memberId")
    List<Favorite> findAllByMemberId(@Param("memberId") Long memberId);

    @Override
    @EntityGraph(attributePaths = {"source", "target"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Favorite> findById(Long id);

    boolean existsByIdAndMember(Long id, Member member);
}
