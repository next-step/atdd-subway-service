package nextstep.subway.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	List<Favorite> findAllByMember_Id(Long id);
	Optional<Favorite> findByIdAndMember_Id(Long id, Long memberId);
	boolean existsFavoriteByMember_IdAndSource_IdAndTarget_Id(Long memberId, Long sourceId, Long targetId);
}
