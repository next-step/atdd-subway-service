package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteSectionRepository extends JpaRepository<FavoriteSection, Long> {
    Optional<FavoriteSection> findByIdAndMemberId(Long deleteFavoriteSectionId, Long memberId);
}
