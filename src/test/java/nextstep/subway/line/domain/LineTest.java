package nextstep.subway.line.domain;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Line도메인의 ")
public class LineTest {

    @Nested
    @DisplayName("isEmptySections메소드는 ")
    class DescribeIsEmptySection{

        @Nested
        @DisplayName("추가된 섹션이 없으면 true를 반환")
        class ContextWithNoSection{

            private final Line line = new Line();

            @Test
            void returnsTrue(){
                assertThat(line.isEmptySections()).isTrue();
            }
        }

        @Nested
        @DisplayName("추가된 섹션이 하나이상이면 false를 반환")
        class Context_with_section{

            private Line line = new Line();
            private Random random = new Random();

            @BeforeEach
            void setUpSection(){
                List<Section> sections = IntStream
                        .rangeClosed(1, random.nextInt(100) + 1)
                        .mapToObj(value -> new Section())
                        .collect(Collectors.toList());
                line.getSections().addAll(sections);
            }

            @Test
            @RepeatedTest(100)
            void returnFalse(){
                assertThat(line.isEmptySections()).isFalse();
            }
        }

    }
}
