package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {
    Sections sections;

    @BeforeEach
    void setup() {
        sections = new Sections();
    }

    @DisplayName("구간 add")
    @Test
    void add() {
        //given
        //when
        sections.add(new Section());
        //then
        assertThat(sections.isEmpty()).isFalse();
    }

    @DisplayName("구간 일급 컬렉션 빈 값 리턴")
    @Test
    void isEmpty() {
        //given
        //when
        //then
        assertThat(sections.isEmpty()).isTrue();
    }

    @DisplayName("사이즈 구하기")
    @Test
    void size() {
        //given
        //when
        sections.add(new Section());
        //then
        assertThat(sections.size()).isEqualTo(1);
    }
}