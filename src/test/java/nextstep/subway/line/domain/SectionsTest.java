package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {
    private Line line = new Line();
    private Station station1 = new Station("강남역");
    private Station station2 = new Station("잠실역");
    private Station station3 = new Station("교대역");
    private Station station4 = new Station("선릉역");

    @Nested
    @DisplayName("addSection 메서드는")
    class Describe_addSection {

        @Nested
        @DisplayName("주어진 구간 목록과 추가할 구간 정보가 주어지면")
        class Context_with_sections_and_section {
            final Sections givenSections = new Sections();
            final Line newLine = line;
            final Station newStation1 = station1;
            final Station newStation2 = station2;
            final int newDistance = 10;

            @Test
            @DisplayName("새로운 구간이 추가된다")
            void it_add_new_section() {
                givenSections.addSection(newLine, newStation1, newStation2, newDistance);

                assertThat(givenSections.getStations()).containsExactly(newStation1, newStation2);
            }
        }

        @Nested
        @DisplayName("주어진 구간과 동일한 구간이 주어지면")
        class Context_with_same_section {
            final Sections givenSections = new Sections();
            final Line newLine = line;
            final Station duplicateStation1 = station1;
            final Station duplicateStation2 = station2;
            final int newDistance = 10;

            @BeforeEach
            void setUp() {
                givenSections.addSection(line, station1, station2, 20);
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() ->
                        givenSections.addSection(newLine, duplicateStation1, duplicateStation2, newDistance))
                        .isInstanceOf(IllegalStateException.class);
            }
        }

        @Nested
        @DisplayName("주어진 구간에 포함되지 않은 역이 주어지면")
        class Context_with_no_match_stations {
            final Sections givenSections = new Sections();
            final Line newLine = line;
            final Station newStation1 = station3;
            final Station newStation2 = station4;
            final int newDistance = 10;

            @BeforeEach
            void setUp() {
                givenSections.addSection(line, station1, station2, 20);
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() ->
                        givenSections.addSection(newLine, newStation1, newStation2, newDistance))
                        .isInstanceOf(IllegalStateException.class);
            }
        }
    }

    @Nested
    @DisplayName("removeSection 메서드는")
    class Describe_removeSection {

        @Nested
        @DisplayName("주어진 구간 목록과 삭제할 역 정보가 주어지면")
        class Context_with_sections_and_station {
            final Sections givenSections = new Sections();
            final Station removeStation = station3;

            @BeforeEach
            void setUp() {
                givenSections.addSection(line, station1, station2, 20);
                givenSections.addSection(line, station1, station3, 10);
            }

            @Test
            @DisplayName("구간 정보가 삭제된다")
            void it_removes_section() {
                givenSections.removeSection(line, removeStation);

                assertThat(givenSections.getStations()).containsExactly(station1, station2);
            }
        }

        @Nested
        @DisplayName("주어진 구간이 한 구간만 존재하면")
        class Context_with_one_section {
            final Sections givenSections = new Sections();
            final Station removeStation = station1;

            @BeforeEach
            void setUp() {
                givenSections.addSection(line, station1, station2, 20);
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() ->
                        givenSections.removeSection(line, removeStation))
                        .isInstanceOf(IllegalStateException.class);
            }
        }
    }
}
