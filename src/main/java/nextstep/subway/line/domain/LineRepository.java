package nextstep.subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LineRepository extends JpaRepository<Line, Long> {
    @Query("select line from Line as line join fetch line.section.sections as section join fetch section.upStation join fetch section.downStation")
    List<Line> findAllJoinFetch();
}
