package nextstep.subway.line.unit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class LineRepositoryTest {
    @Autowired
    private LineRepository lineRepository;

    @DisplayName("중복된 이름으로 노선을 생성하고자 하면 DataIntegrityViolationException 발생한다.")
    @Test
    void save_duplicatedName_exception() {

        // when
        lineRepository.save(new Line("신분당선", "빨간색"));
        
        // then
        assertThatThrownBy(() -> lineRepository.save(new Line("신분당선", "파란색")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
