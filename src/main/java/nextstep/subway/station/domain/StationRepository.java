package nextstep.subway.station.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface StationRepository extends JpaRepository<Station, Long> {
    @Override
    List<Station> findAll();

    @Query(value = "select distinct s from Station s left join Favorite f1 on s.id = f1.sourceStationId left join Favorite f2 on s.id = f2.targetStationId where f1.memberId = :memberId or f2.memberId = :memberId")
    Set<Station> findFavoriteStationByMemberId(@Param("memberId") Long memberId);
}
