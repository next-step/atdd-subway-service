package nextstep.subway.favorite.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	List<Favorite> findByMember(Member member);

	boolean existsByMemberAndSourceStationAndTargetStation(Member member, Station sourceStation, Station targetStation);
}
