package nextstep.subway.favorite.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	List<Favorite> findAllByMemberId(Long memberId);
	Optional<Favorite> findByIdAndMember_Id(Long favoriteId, Long memberId);
}
