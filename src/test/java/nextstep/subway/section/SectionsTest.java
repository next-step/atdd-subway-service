package nextstep.subway.section;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Sections도메인의 ")
public class SectionsTest {

    @Nested
    @DisplayName("size메소드는 ")
    class DescribeSize {

        @Nested
        @DisplayName("노선개수를 반환")
        class ContextWithSection {

            private final Sections sections = new Sections();
            private int size;
            private Random random = new Random();

            @BeforeEach
            void setUpSection() {
                size = random.nextInt(100);
                IntStream.rangeClosed(1, size)
                        .forEach(value -> sections.addSection(new Section()));
            }

            @RepeatedTest(100)
            void returnsSize() {
                assertThat(sections.size()).isEqualTo(size);
            }
        }
    }
}
