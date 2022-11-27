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
}
