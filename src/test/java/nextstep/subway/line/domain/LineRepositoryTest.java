package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import javax.persistence.NoResultException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LineRepositoryTest {

    @Autowired
    LineRepository lineRepository;

    @DisplayName("노선을 신규 등록할 수 있다.")
    @Test
    void create() {
        Line line = new Line("신분당선", "red");

        Line actual = lineRepository.save(line);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getColor()).isEqualTo(line.getColor())
        );
    }

    @DisplayName("노선을 조회할 수 있다.")
    @Test
    void read() {
        Line actual = lineRepository.save(new Line("신분당선", "red"));

        Optional<Line> expect = lineRepository.findById(actual.getId());

        assertThat(expect).isPresent();
    }

    @DisplayName("노선을 이름과 색을 변경할 수 있다.")
    @Test
    void update() {
        Line 신분당선 = new Line("신분당선", "red");
        Line 구분당선 = new Line("구분당선", "blue");
        Line actual = lineRepository.save(신분당선);

        actual.update(구분당선);
        Optional<Line> expect = lineRepository.findById(actual.getId());

        assertAll(
                () -> assertThat(expect.orElseThrow(NoResultException::new).getName()).isEqualTo(구분당선.getName()),
                () -> assertThat(expect.orElseThrow(NoResultException::new).getColor()).isEqualTo(구분당선.getColor())
        );
    }

    @DisplayName("노선을 삭제할 수 있다.")
    @Test
    void delete() {
        Line line = lineRepository.save(new Line("신분당선", "red"));

        lineRepository.delete(line);
        Optional<Line> actual = lineRepository.findById(line.getId());

        assertThat(actual).isNotPresent();
    }
}
