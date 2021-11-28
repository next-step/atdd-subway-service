package nextstep.subway.line.domain.section;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query("select sec from Section sec " +
            "left join fetch sec.upStation " +
            "left join fetch sec.downStation ")
    List<Section> findAll();
}
