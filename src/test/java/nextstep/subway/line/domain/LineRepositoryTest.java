package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineFixtures.이호선;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LineRepositoryTest {

    @Autowired
    LineRepository lineRepository;

    @Test
    @DisplayName("노선 삭제")
    void deleteById() {
        // when
        Line persistLine = lineRepository.save(이호선);

        // then
        assertThat(persistLine.getId()).isNotNull();

        // when
        lineRepository.deleteById(persistLine.getId());
        boolean actual = lineRepository.findById(persistLine.getId()).isPresent();

        // then
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("지하철 역이름 유무 체크")
    void existsByName() {
        // given
        Line persistLine = lineRepository.save(이호선);

        // when
        boolean actual = lineRepository.existsByName(persistLine.getName());

        // then
        assertThat(actual).isTrue();
    }
}
