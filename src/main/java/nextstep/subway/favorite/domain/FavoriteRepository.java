package nextstep.subway.favorite.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.subway.member.domain.Member;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByIdAndMember(Long id, Member member);

    List<Favorite> findAllByMember(Member member);
}
