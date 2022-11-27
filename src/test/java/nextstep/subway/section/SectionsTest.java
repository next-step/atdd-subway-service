package nextstep.subway.section;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Sections도메인의 ")
public class SectionsTest {

    @Nested
    @DisplayName("size메소드는 ")
    class DescribeSize {

        @Nested
        @DisplayName("노선개수를 반환")
        class ContextWithSection {

            private final Sections sections = new Sections();
            private final Line line = new Line();

            @BeforeEach
            void before() {
                sections.addSection(new Section(line, new Station("영등포역"), new Station("신림역"), 10));
                sections.addSection(new Section(line, new Station("신림역"), new Station("방배역"), 10));
                sections.addSection(new Section(line, new Station("방배역"), new Station("삼성역"), 10));
                sections.addSection(new Section(line, new Station("삼성역"), new Station("신도림역"), 10));
            }

            @Test
            void returnsSize() {
                assertThat(sections.size()).isEqualTo(4);
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
                assertThat(sections.findUpStation()).isEqualTo(station);
            }
        }
    }

    @Nested
    @DisplayName("getStations메소드는 ")
    class DescribeGetStations {

        @Nested
        @DisplayName("모든 역을 노선순서에 따라 중복없이 반환")
        class ContextWithSections {

            private final Line line = new Line();
            private final Sections sections = new Sections();

            @BeforeEach
            void before() {
                sections.addSection(new Section(line,  new Station("강남역"), new Station("신림역"), 10));
                sections.addSection(new Section(line, new Station("신림역"), new Station("방배역"), 10));
            }

            @Test
            void returnStation() {
                assertThat(sections.getStations()).containsAnyOf(new Station("강남역"), new Station("신림역"), new Station("방배역"));
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

            private final Sections sections = new Sections();
            private final Line line = new Line();

            @BeforeEach
            void before() {
                List<Section> sections = IntStream
                        .rangeClosed(1, random.nextInt(1))
                        .mapToObj(value -> new Section())
                        .collect(Collectors.toList());
                sections.addAll(sections);
            }

            @Test
            void throwsException() {
                assertThatThrownBy(() -> sections.removeStation(new Station(),line))
                        .isInstanceOf(RuntimeException.class);
            }
        }

        @Nested
        @DisplayName("기존 노선중에 상행종점과 하행종점에 모두 포함될경우 기존 노선을 지우고 새로운 노선추가")
        class ContextWithUpStationAndDownStation {

            private Sections sections = new Sections();
            private Line line = new Line();
            private Station station = new Station("학동역");

            @BeforeEach
            void before() {
                sections.addSection(new Section(line, new Station("숭실대입구"), station, 10));
                sections.addSection(new Section(line, station, new Station("건대입구역"), 10));
            }

            @Test
            void returnNewSection() {
                sections.removeStation(station,line);

                List<String> stationNames = sections.getStations().stream()
                        .map(Station::getName)
                        .collect(Collectors.toList());
                assertThat(stationNames).containsExactlyInAnyOrder("숭실대입구", "건대입구역");
            }
        }

        @Nested
        @DisplayName("기존 노선중에 상행종점과 하행종점 하나만 포함될경우 해당 노선을 삭제")
        class ContextWithUpStationOrDownStation {

            private Sections sections = new Sections();
            private Line line = new Line();
            private Station station = new Station("학동역");

            @BeforeEach
            void before() {
                sections.addSection(new Section(line, station, new Station("세종대"), 10));
                sections.addSection(new Section(line, new Station("세종대"), new Station("건대입구역"), 10));
            }

            @Test
            void returnRemainsSection() {
                sections.removeStation(station,line);

                List<String> stationNames = sections.getStations().stream()
                        .map(Station::getName)
                        .collect(Collectors.toList());
                assertThat(stationNames).containsExactlyInAnyOrder("세종대", "건대입구역");
            }
        }
    }

    @Nested
    @DisplayName("addSection메소드는 ")
    class DescribeAddSection {

        @Nested
        @DisplayName("이미 등록된 구간이면 예외발생")
        class ContextWithUpStationAndDownStation {

            private final Sections sections = new Sections();
            private final Line line = new Line();

            @BeforeEach
            void before() {
                sections.addSection(new Section(line,new Station("강동역"),new Station("길동역"),10));
            }

            @Test
            void throwsException() {
                assertThatThrownBy(() -> sections.addSection(new Section(line,new Station("강동역"),new Station("길동역"),10)))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("이미 등록된 구간 입니다.");
            }
        }

        @Nested
        @DisplayName("구간중에 등록된 역이 하나도 없으면 예외발생")
        class ContextWithNoSection {

            private final Sections sections = new Sections();
            private final Line line = new Line();

            @BeforeEach
            void before() {
                sections.addSection(new Section(line,new Station("강동역"),new Station("길동역"),10));
            }

            @Test
            void returnNewSection() {
                assertThatThrownBy(() -> sections.addSection(new Section(line,new Station("강남역"),new Station("삼성역"),10)))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("등록할 수 없는 구간 입니다.");
            }
        }

        @Nested
        @DisplayName("역사이에 새로운역을 등록할 경우 새로운 길이를 뺸 나머지를 새롭게 추가된 역과 길이로 설정")
        class ContextWithAmongStations {

            private final Line line = new Line();
            private final Sections sections = new Sections();
            private final Section section = new Section(line, new Station("신촌역"), new Station("종합운동장"), 10);

            @BeforeEach
            void before() {
                sections.addSection(section);
                sections.addSection(new Section(line,new Station("종합운동장"), new Station("홍대입구역"), 10));
            }

            @Test
            void returnsStations() {
                sections.addSection(new Section(line,new Station("신촌역"), new Station("방배역"), 3));
                assertAll(
                        () -> assertThat(section.getUpStation()).isEqualTo(new Station("방배역")),
                        () -> assertThat(section.getDownStation()).isEqualTo(new Station("종합운동장")),
                        () -> assertThat(section.getDistance()).isEqualTo(7),
                        () -> assertThat(sections.getStations()).containsAnyOf(new Station("신촌역"),
                                new Station("종합운동장"),
                                new Station("홍대입구역"),
                                new Station("방배역"))
                );
            }
        }

        @Nested
        @DisplayName("종점에 등록할 경우 기존 노선에 추가")
        class ContextWithEdgeStation {

            private final Line line = new Line();
            private final Sections sections = new Sections();

            @BeforeEach
            void before() {
                sections.addSection(new Section(line,new Station("신촌역"), new Station("종합운동장"), 10));
                sections.addSection(new Section(line,new Station("종합운동장"), new Station("홍대입구역"), 10));
            }

            @Test
            void returnsStations() {
                sections.addSection(new Section(line,new Station("영등포역"), new Station("신촌역"), 3));
                assertAll(
                        () -> assertThat(sections.size()).isEqualTo(3),
                        () -> assertThat(sections.getStations()).containsAnyOf(new Station("신촌역"),
                                new Station("종합운동장"),
                                new Station("홍대입구역"),
                                new Station("영등포역"))
                );
            }
        }
    }
}
