package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("select f " +
            "from Favorite f " +
            "join fetch f.source " +
            "join fetch f.target")
    List<Favorite> findAllWithStationsByMember(Member member);

    Optional<Favorite> findByIdAndMember(Long id, Member member);
}
