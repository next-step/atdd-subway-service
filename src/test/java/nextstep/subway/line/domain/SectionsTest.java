package nextstep.subway.line.domain;

import static nextstep.subway.line.step.LineStep.line;
import static nextstep.subway.line.step.SectionStep.section;
import static nextstep.subway.station.step.StationStep.station;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import nextstep.subway.common.domain.Fare;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("구간들")
class SectionsTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Sections.from(양재_광교_구간()));
    }

    @Test
    @DisplayName("초기 구간은 반드시 필수")
    void instance_nullSection_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Sections.from((Section) null))
            .withMessage("초기 구간은 반드시 존재해야 합니다.");
    }

    @Test
    @DisplayName("순서대로 정렬된 역들")
    void stations() {
        // when
        Stations stations = Sections.from(양재_광교_구간()).sortedStations();

        // then
        assertThat(stations)
            .isEqualTo(Stations.from(Arrays.asList(station("양재"), station("광교"))));
    }

    @ParameterizedTest(name = "[{index}] 양재 광교 구간에 {0} 구간을 추가하면 {1} 역들")
    @DisplayName("구간 추가")
    @MethodSource
    void addSection(Section section, Station... expectedStations) {
        // given
        Sections sections = Sections.from(양재_광교_구간());

        // when
        sections.add(section);

        // then
        assertThat(sections.sortedStations())
            .isEqualTo(Stations.from(Arrays.asList(expectedStations)));
    }

    @Test
    @DisplayName("이미 존재하는 역들의 구간 추가")
    void add_duplicateStation_thrownDuplicateDataException() {
        // given
        Sections sections = Sections.from(양재_광교_구간());

        // when
        ThrowingCallable addCall = () -> sections.add(양재_광교_구간());

        // then
        assertThatExceptionOfType(DuplicateDataException.class)
            .isThrownBy(addCall)
            .withMessageEndingWith("이미 등록된 구간 입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 역들의 구간 추가")
    void add_notExistsStation_thrownNotFoundException() {
        // given
        Section 강남_정자_구간 = Section.of(
            station("강남"),
            station("정자"),
            Distance.from(5));

        Sections sections = Sections.from(양재_광교_구간());

        //when
        ThrowingCallable addCall = () -> sections.add(강남_정자_구간);

        //then
        assertThatExceptionOfType(InvalidDataException.class)
            .isThrownBy(addCall)
            .withMessageEndingWith("등록할 수 없는 구간 입니다.");
    }

    @ParameterizedTest(name = "[{index}] 100, 500, {0} 추가 요금의 노선 구간들 중 가장 큰 추가 요금은 {1}")
    @CsvSource({"50,500", "300,500", "1000,1000"})
    @DisplayName("최대 추가 요금")
    void maxExtraFare(int extraFare, int expected) {
        // given
        Sections sections = Lines.from(Arrays.asList(
            anyLine(100),
            anyLine(500),
            anyLine(extraFare)
        )).sections();

        // when
        Fare maxExtraFare = sections.maxExtraFare();

        // then
        assertThat(maxExtraFare).isEqualTo(Fare.from(expected));
    }

    @Test
    @DisplayName("최대 추가 요금을 구하려면 구간들이 반드시 존재")
    void maxExtraFare_empty_thrownInvalidDataException() {
        // given
        Sections emptySections = Sections.from(Collections.emptyList());

        // when
        ThrowingCallable maxExtraFareCallable = emptySections::maxExtraFare;

        // then
        assertThatExceptionOfType(InvalidDataException.class)
            .isThrownBy(maxExtraFareCallable)
            .withMessage("비어있는 구간들에서 추가 요금을 계산할 수 없습니다.");
    }

    @Nested
    @DisplayName("역 삭제")
    @TestInstance(Lifecycle.PER_CLASS)
    class StationRemovalTest {

        @ParameterizedTest(name = "[{index}] 강남 양재 광교 구간에서 {0} 역을 제거하면 {1} 역들")
        @MethodSource
        @DisplayName("역 삭제")
        void removeStation(Station station, Station... expectedStations) {
            Sections 강남_양재_광교_구간들 = 강남_양재_광교_구간들();

            // when
            강남_양재_광교_구간들.removeStation(station);

            // then
            assertThat(강남_양재_광교_구간들.sortedStations())
                .isEqualTo(Stations.from(Arrays.asList(expectedStations)));
        }

        @Test
        @DisplayName("한 구간만 남아있는 경우 역 삭제")
        void removeStation_remainedLastSection_thrownInvalidDataException() {
            //given
            Sections 강남_양재_광교_구간들 = 강남_양재_광교_구간들();
            강남_양재_광교_구간들.removeStation(station("강남"));

            //when
            ThrowingCallable removeStationCall =
                () -> 강남_양재_광교_구간들.removeStation(station("양재"));

            //then
            assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(removeStationCall)
                .withMessageEndingWith("구간은 반드시 한 개 이상 존재해야 합니다.");
        }

        private Sections 강남_양재_광교_구간들() {
            Sections sections = Sections.from(양재_광교_구간());
            sections.add(Section.of(station("강남"), station("양재"), Distance.from(10)));
            return sections;
        }

        private Stream<Arguments> removeStation() {
            return Stream.of(
                Arguments.of(station("강남"), new Station[]{station("양재"), station("광교")}),
                Arguments.of(station("양재"), new Station[]{station("강남"), station("광교")}),
                Arguments.of(station("광교"), new Station[]{station("강남"), station("양재")})
            );
        }
    }

    private static Stream<Arguments> addSection() {
        return Stream.of(
            Arguments.of(
                Section.of(station("강남"), station("양재"), Distance.from(10)),
                new Station[]{station("강남"), station("양재"), station("광교")}
            ),
            Arguments.of(
                Section.of(station("양재"), station("정자"), Distance.from(10)),
                new Station[]{station("양재"), station("정자"), station("광교")}
            ),
            Arguments.of(
                Section.of(station("정자"), station("광교"), Distance.from(10)),
                new Station[]{station("양재"), station("정자"), station("광교")}
            ),
            Arguments.of(
                Section.of(station("광교"), station("판교"), Distance.from(10)),
                new Station[]{station("양재"), station("광교"), station("판교")}
            )
        );
    }

    private Section 양재_광교_구간() {
        return section("양재", "광교", 20);
    }

    private Line anyLine(int extraFare) {
        return line("any", "red", section("any1", "any2", 10), extraFare);
    }
}
