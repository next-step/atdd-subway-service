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
            void before() {
                List<Section> sections = IntStream
                        .rangeClosed(1, random.nextInt(100) + 1)
                        .mapToObj(value -> new Section())
                        .collect(Collectors.toList());
                line.getSections().addAll(sections);
            }

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
            void before() {
                List<Section> sections = IntStream
                        .rangeClosed(1, random.nextInt(1))
                        .mapToObj(value -> new Section())
                        .collect(Collectors.toList());
                line.getSections().addAll(sections);
            }

            @RepeatedTest(100)
            void returnsTrue() {
                assertThat(line.isUnderSingleSection()).isTrue();
            }
        }

        @Nested
        @DisplayName("노선개수가 2개 이상이면 false를 반환")
        class ContextWithSection {

            private Line line = new Line();

            @BeforeEach
            void before() {
                List<Section> sections = IntStream
                        .rangeClosed(1, random.nextInt(100) + 2)
                        .mapToObj(value -> new Section())
                        .collect(Collectors.toList());
                line.getSections().addAll(sections);
            }

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
            void before() {
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

        @Nested
        @DisplayName("기존 노선중에 상행종점과 하행종점에 모두 포함될경우 기존 노선을 지우고 새로운 노선추가")
        class ContextWithUpStationAndDownStation {

            private Line line = new Line();
            private Station station = new Station("학동역");

            @BeforeEach
            void before() {
                line.getSections().add(new Section(line, new Station("숭실대입구"), station, 10));
                line.getSections().add(new Section(line, station, new Station("건대입구역"), 10));
            }

            @Test
            void returnNewSection() {
                line.removeStation(station);

                List<String> upStationNames = line.getSections().stream()
                        .map(Section::getUpStation)
                        .map(Station::getName)
                        .collect(Collectors.toList());
                List<String> downStationName = line.getSections().stream()
                        .map(Section::getDownStation)
                        .map(Station::getName)
                        .collect(Collectors.toList());
                upStationNames.addAll(downStationName);
                assertThat(upStationNames).containsExactlyInAnyOrder("숭실대입구", "건대입구역");
            }
        }

        @Nested
        @DisplayName("기존 노선중에 상행종점과 하행종점 하나만 포함될경우 해당 노선을 삭제")
        class ContextWithUpStationOrDownStation {

            private Line line = new Line();
            private Station station = new Station("학동역");

            @BeforeEach
            void before() {
                line.getSections().add(new Section(line, new Station("숭실대입구"), station, 10));
                line.getSections().add(new Section(line, new Station("세종대"), new Station("건대입구역"), 10));
            }

            @Test
            void returnRemainsSection() {
                line.removeStation(station);

                List<String> upStationNames = line.getSections().stream()
                        .map(Section::getUpStation)
                        .map(Station::getName)
                        .collect(Collectors.toList());
                List<String> downStationName = line.getSections().stream()
                        .map(Section::getDownStation)
                        .map(Station::getName)
                        .collect(Collectors.toList());
                upStationNames.addAll(downStationName);
                assertThat(upStationNames).containsExactlyInAnyOrder("세종대", "건대입구역");
            }
        }
    }

    @Nested
    @DisplayName("findUpStation메소드는 ")
    class DescribeFindUpStation {
        @Nested
        @DisplayName("제일 앞단의 상행종점 반환")
        class ContextWith {

            private final Line line = new Line();
            private final Random random = new Random();
            private final Station station = new Station("강남역");

            @BeforeEach
            void before() {
                line.getSections().add(new Section(line, station, new Station("신촌역"), 10));
                line.getSections().add(new Section(line, new Station("삼성역"), new Station("종합운동장"), 10));
                line.getSections().add(new Section(line, new Station("잠실역"), new Station("홍대입구역"), 10));
            }

            @Test
            void returnsStations() {
                assertThat(line.findUpStation()).isEqualTo(station);
            }

            @AfterEach
            void after() {
                line.getSections().clear();
            }
        }
    }

}
