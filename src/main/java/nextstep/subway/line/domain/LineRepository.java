package nextstep.subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Query("select l from Line l where l.id in :ids")
    List<Line> findAllByIds(@Param("ids") List<Long> ids);
}
