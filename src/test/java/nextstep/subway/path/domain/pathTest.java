package nextstep.subway.path.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 도메인 단위테스트")
public class pathTest {

    @BeforeEach
    public void setUp() {
        // given
    }

    @Test
    @DisplayName("지하철 경로")
    void findPath() {
        assertThat(true).isEqualTo(true);
    }
}