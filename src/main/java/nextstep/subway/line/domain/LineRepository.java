package nextstep.subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.subway.common.domain.Name;

public interface LineRepository extends JpaRepository<Line, Long> {

	boolean existsByName(Name name);
}
