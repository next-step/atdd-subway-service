package nextstep.subway.favorite.domain;


import java.util.Optional;
import nextstep.subway.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByIdAndMember(long id, Member member);
}
