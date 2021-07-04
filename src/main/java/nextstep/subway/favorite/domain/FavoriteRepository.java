package nextstep.subway.favorite.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.subway.favorite.FavoriteSection;
import nextstep.subway.favorite.dto.FavoriteResponse;

public interface FavoriteRepository extends JpaRepository<FavoriteSection, Long> {
    List<FavoriteResponse> findAllByMemberId(Long id);
}
