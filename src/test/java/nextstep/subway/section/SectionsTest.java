package nextstep.subway.section;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.*;

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
            void before() {
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

    @Nested
    @DisplayName("findUpStation메소드는 ")
    class DescribeFindUp {

        @Nested
        @DisplayName("노선에서 상행종점역을 반환")
        class ContextWithSections {

            private final Line line = new Line();
            private final Sections sections = new Sections();
            private final Station station = new Station("강남역");

            @BeforeEach
            void before() {
                sections.addSection(new Section(line, station, new Station("신림역"), 10));
                sections.addSection(new Section(line, new Station("신림역"), new Station("방배역"), 10));
                sections.addSection(new Section(line, new Station("방배역"), new Station("삼성역"), 10));
                sections.addSection(new Section(line, new Station("삼성역"), new Station("신도림역"), 10));
            }

            @Test
            void returnStation() {
                sections.findUpStation().equals(station);
            }
        }
    }
}
