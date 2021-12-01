package nextstep.subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<Line, Long> {
    default Line findByIdElseThrow(Long id) {
        return this.findById(id).orElseThrow(RuntimeException::new);
    }
}
