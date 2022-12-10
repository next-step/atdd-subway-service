package nextstep.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 세션 도메인 테스트")
public class SectionTest {
    private Section section;
    @BeforeEach
    void setUp() {
        section = new Section(new TestStation("강남"), new TestStation("양평"), 3);
    }
    @Test
    @DisplayName("isSameDownStation 테스트- 성공")
    public void isSameDownStation() {
        TestStation actual = new TestStation("양평");

        assertThat(section.isSameDownStation(actual)).isTrue();
    }

    @Test
    @DisplayName("isSameDownStation 테스트 - 실패")
    public void isSameDownStation2() {
        TestStation actual = new TestStation("강남");

        assertThat(section.isSameDownStation(actual)).isFalse();
    }

    @Test
    @DisplayName("isSameUpStation 테스트 - 성공")
    public void isSameUpStation() {
        TestStation actual = new TestStation("강남");

        assertThat(section.isSameUpStation(actual)).isTrue();
    }

    @Test
    @DisplayName("isSameDownStation 테스트 - 실패")
    public void isSameUpStation2() {
        TestStation actual = new TestStation("양평");

        assertThat(section.isSameUpStation(actual)).isFalse();
    }
}
