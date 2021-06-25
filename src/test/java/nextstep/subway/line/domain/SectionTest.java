package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionTest {
    private static final int BEFORE_DISTANCE = 10;
    private static final int NEW_DISTANCE = 5;
    private static final int LONG_DISTANCE = 20;

    private Line line = new Line();
    private Station station1 = new Station("강남역");
    private Station station2 = new Station("잠실역");
    private Station station3 = new Station("교대역");
    private Section section = Section.of(line, station1, station2, BEFORE_DISTANCE);

    @Nested
    @DisplayName("updateUpStation 메서드는")
    class Describe_updateUpStation {

        @Nested
        @DisplayName("주어진 구간과 갱신할 역 정보가 주어지면")
        class Context_with_section_and_station {
            final Section givenSection = section;
            final Station newStation = station3;
            final int newDistance = NEW_DISTANCE;

            @Test
            @DisplayName("상행역과 거리가 갱신된다")
            void it_update_up_station_and_distance() {
                givenSection.updateUpStation(newStation, newDistance);

                assertThat(givenSection.getUpStation()).isEqualTo(newStation);
                assertThat(givenSection.getDistance()).isEqualTo(newDistance);
            }
        }

        @Nested
        @DisplayName("주어진 구간 거리보다 더 긴 갱신정보가 주어지면")
        class Context_with_section_and_long_distance {
            final Section givenSection = section;
            final Station newStation = station3;
            final int newDistance = LONG_DISTANCE;

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> givenSection.updateUpStation(newStation, newDistance))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("updateDownStation 메서드는")
    class Describe_updateDownStation {

        @Nested
        @DisplayName("주어진 구간과 갱신할 역 정보가 주어지면")
        class Context_with_section_and_station {
            final Section givenSection = section;
            final Station newStation = station3;
            final int newDistance = NEW_DISTANCE;

            @Test
            @DisplayName("하행역과 거리가 갱신된다")
            void it_update_down_station_and_distance() {
                givenSection.updateDownStation(newStation, newDistance);

                assertThat(givenSection.getDownStation()).isEqualTo(newStation);
                assertThat(givenSection.getDistance()).isEqualTo(newDistance);
            }
        }

        @Nested
        @DisplayName("주어진 구간 거리보다 더 긴 갱신정보가 주어지면")
        class Context_with_section_and_long_distance {
            final Section givenSection = section;
            final Station newStation = station3;
            final int newDistance = LONG_DISTANCE;

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> givenSection.updateDownStation(newStation, newDistance))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
