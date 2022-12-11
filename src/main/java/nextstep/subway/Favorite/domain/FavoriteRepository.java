package nextstep.subway.Favorite.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	Optional<Favorite> findByIdAndMemberId(long anyLong, Long eq);
}
