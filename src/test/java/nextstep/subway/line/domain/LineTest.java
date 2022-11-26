package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Line도메인의 ")
public class LineTest {

    @Nested
    @DisplayName("isEmptySections메소드는 ")
    class DescribeIsEmptySection {

        @Nested
        @DisplayName("추가된 노선이 없으면 true를 반환")
        class ContextWithNoSection {

            private final Line line = new Line();

            @Test
            void returnsTrue() {
                assertThat(line.isEmptySections()).isTrue();
            }
        }

        @Nested
        @DisplayName("추가된 노선이 하나이상이면 false를 반환")
        class ContextWithSection {

            private Line line = new Line();
            private Random random = new Random();

            @BeforeEach
            void setUpSection() {
                List<Section> sections = IntStream
                        .rangeClosed(1, random.nextInt(100) + 1)
                        .mapToObj(value -> new Section())
                        .collect(Collectors.toList());
                line.getSections().addAll(sections);
            }

            @Test
            @RepeatedTest(100)
            void returnFalse() {
                assertThat(line.isEmptySections()).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("isUnderSingleSection메소드는 ")
    class DescribeIsUnderSingleSection {

        private Random random = new Random();

        @Nested
        @DisplayName("노선개수가 1개 이하이면 true를 반환")
        class ContextWithNoSection {

            private final Line line = new Line();

            @BeforeEach
            void setUpSection() {
                List<Section> sections = IntStream
                        .rangeClosed(1, random.nextInt(1))
                        .mapToObj(value -> new Section())
                        .collect(Collectors.toList());
                line.getSections().addAll(sections);
            }

            @Test
            void returnsTrue() {
                assertThat(line.isUnderSingleSection()).isTrue();
            }
        }

        @Nested
        @DisplayName("노선개수가 2개 이상이면 false를 반환")
        class ContextWithSection {

            private Line line = new Line();

            @BeforeEach
            void setUpSection() {
                List<Section> sections = IntStream
                        .rangeClosed(1, random.nextInt(100) + 2)
                        .mapToObj(value -> new Section())
                        .collect(Collectors.toList());
                line.getSections().addAll(sections);
            }

            @Test
            @RepeatedTest(100)
            void returnFalse() {
                assertThat(line.isUnderSingleSection()).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("removeStation메소드는 ")
    class DescribeRemoveStation {

        private Random random = new Random();

        @Nested
        @DisplayName("노선개수가 1개 이하이면 예외발생")
        class ContextWithUnderSingleSection {

            private final Line line = new Line();

            @BeforeEach
            void setUpSection() {
                List<Section> sections = IntStream
                        .rangeClosed(1, random.nextInt(1))
                        .mapToObj(value -> new Section())
                        .collect(Collectors.toList());
                line.getSections().addAll(sections);
            }

            @Test
            void throwsException() {
                assertThatThrownBy(() -> line.removeStation(new Station()))
                        .isInstanceOf(RuntimeException.class);
            }
        }
/*
        @Nested
        @DisplayName("기존 노선중에 상행종점과 하행종점에 모두 포함될경우 기존 노선을 지우고 새로운 노선추가")
        class ContextWithUpStationAndDownStation {

            private Line line = new Line();

            @BeforeEach
            void setUpSection() {
                List<Section> sections = IntStream
                        .rangeClosed(1, random.nextInt(100) + 2)
                        .mapToObj(value -> new Section())
                        .collect(Collectors.toList());
                line.getSections().addAll(sections);
            }

            @Test
            @RepeatedTest(100)
            void returnFalse() {
                assertThat(line.isUnderSingleSection()).isFalse();
            }
        }*/
    }
}
