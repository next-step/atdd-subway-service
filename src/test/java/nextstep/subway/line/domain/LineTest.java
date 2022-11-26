package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    @Nested
    @DisplayName("getStations메소드는 ")
    class DescribeGetStations {

        @DisplayName("노선이 없으면 빈배열 반환")
        @Test
        void returnEmptyArray() {
            assertThat(new Line().getStations()).isEmpty();
        }

        @Nested
        @DisplayName("노선이 존재하면 노선을 구성하는 모든역을 반환")
        class ContextWith {

            private final Line line = new Line();

            @BeforeEach
            void before() {
                line.getSections().add(new Section(line, new Station("강남역"), new Station("신촌역"), 10));
                line.getSections().add(new Section(line, new Station("신촌역"), new Station("종합운동장"), 10));
                line.getSections().add(new Section(line, new Station("종합운동장"), new Station("홍대입구역"), 10));
            }

            @Test
            void returnsStations() {
                assertThat(line.getStations().stream().map(station -> station.getName()))
                        .containsExactlyInAnyOrder("강남역", "신촌역", "종합운동장", "홍대입구역");
            }
        }
    }

    @Nested
    @DisplayName("addSection메소드는 ")
    class DescribeAddSection {

        @Nested
        @DisplayName("두개의 역이 이미 등록되있는경우 예외발생")
        class ContextWithUpStationAndDownStation {

            private final Line line = new Line();

            @BeforeEach
            void before() {
                line.getSections().add(new Section(line, new Station("강남역"), new Station("신촌역"), 10));
                line.getSections().add(new Section(line, new Station("신촌역"), new Station("종합운동장"), 10));
                line.getSections().add(new Section(line, new Station("종합운동장"), new Station("홍대입구역"), 10));
            }

            @Test
            void throwsException() {
                assertThatThrownBy(() -> line.addSection(new Station("강남역"), new Station("신촌역"), 10))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("이미 등록된 구간 입니다.");
            }
        }

        @Nested
        @DisplayName("등록된 역이 하나도 없으면 예외발생")
        class ContextWithNoStation {

            private final Line line = new Line();

            @BeforeEach
            void before() {
                line.getSections().add(new Section(line, new Station("신촌역"), new Station("종합운동장"), 10));
                line.getSections().add(new Section(line, new Station("종합운동장"), new Station("홍대입구역"), 10));
            }

            @Test
            void throwsException() {
                assertThatThrownBy(() -> line.addSection(new Station("강남역"), new Station("영등포역"), 10))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("등록할 수 없는 구간 입니다.");
            }
        }

        @Nested
        @DisplayName("역사이에 새로운역을 등록할 경우 새로운 길이를 뺸 나머지를 새롭게 추가된 역과 길이로 설정")
        class ContextWithAmongStations {

            private final Line line = new Line();
            private final Section section = new Section(line, new Station("신촌역"), new Station("종합운동장"), 10);

            @BeforeEach
            void before() {
                line.getSections().add(section);
                line.getSections().add(new Section(line, new Station("종합운동장"), new Station("홍대입구역"), 10));
            }

            @Test
            void returnsStations() {
                line.addSection(new Station("신촌역"), new Station("방배역"), 3);
                assertAll(
                        () -> assertThat(line.getSections()).hasSize(3),
                        () -> assertThat(section.getUpStation()).isEqualTo(new Station("방배역")),
                        () -> assertThat(section.getDownStation()).isEqualTo(new Station("종합운동장")),
                        () -> assertThat(section.getDistance()).isEqualTo(7),
                        () -> assertThat(line.getStations()).containsAnyOf(new Station("신촌역"),
                                new Station("종합운동장"),
                                new Station("홍대입구역"),
                                new Station("방배역"))
                );
            }
        }

        @Nested
        @DisplayName("종점에 등록할 경우 기존 노선에 추가")
        class ContextWithDownStation {

            private final Line line = new Line();

            @BeforeEach
            void before() {
                line.getSections().add(new Section(line, new Station("신촌역"), new Station("종합운동장"), 10));
                line.getSections().add(new Section(line, new Station("종합운동장"), new Station("홍대입구역"), 10));
            }

            @Test
            void returnsStations() {
                line.addSection(new Station("영등포역"), new Station("신촌역"), 3);
                assertAll(
                        () -> assertThat(line.getSections()).hasSize(3),
                        () -> assertThat(line.getStations()).containsAnyOf(new Station("신촌역"),
                                new Station("종합운동장"),
                                new Station("홍대입구역"),
                                new Station("영등포역"))
                );
            }
        }
    }
}
