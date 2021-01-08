package nextstep.subway.favorite.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-07
 */
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByMemberId(Long memberId);

}
