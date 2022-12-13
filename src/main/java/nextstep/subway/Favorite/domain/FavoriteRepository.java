package nextstep.subway.Favorite.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	Optional<Favorite> findByIdAndMemberId(long anyLong, Long eq);

	boolean existsBySourceIdAndTargetIdAndMemberId(Long sourceId, Long targetId, long memberId);

	List<Favorite> findAllByMemberId(Long id);
}
