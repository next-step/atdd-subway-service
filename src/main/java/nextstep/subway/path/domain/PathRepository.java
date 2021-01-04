package nextstep.subway.path.domain;

import java.util.Optional;

public interface PathRepository {

    PathSections findAllSections();

    Optional<PathStation> findById(Long id);
}
