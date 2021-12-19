package nextstep.subway.favortie.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.subway.member.domain.Member;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	List<Favorite> findByMember(Member member);
	
}
