package nextstep.subway.favorite.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nextstep.subway.member.domain.Member;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	@Query("select f "
		+ "from Favorite f "
		+ "join fetch f.creator "
		+ "join fetch f.target "
		+ "join fetch f.source "
		+ "where f.creator = :creator")
	List<Favorite> findAllWithStationByCreator(@Param("creator") Member creator);
}
