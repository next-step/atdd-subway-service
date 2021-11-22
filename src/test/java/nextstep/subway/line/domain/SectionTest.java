package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.stream.Stream;
import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("구간")
class SectionTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Section.of(
                station("강남"),
                station("광교"),
                Distance.from(Integer.MAX_VALUE)));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 값 으로 객체화 할 수 없다.")
    @MethodSource
    @DisplayName("'null' 인자가 존재한 상태로 객체화")
    void instance_emptyArgument_thrownIllegalArgumentException(
        Station upStation, Station downStation, Distance distance) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Section.of(upStation, downStation, distance))
            .withMessageEndingWith(" 값은 null 일 수 없습니다.");
    }

    @Test
    @DisplayName("같은 역으로 객체화")
    void instance_sameUpAndDownStation_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() ->
                Section.of(station("강남"),
                    station("강남"),
                    Distance.from(Integer.MAX_VALUE)))
            .withMessageEndingWith(" 같을 수 없습니다.");
    }

    @ParameterizedTest(name = "[{index}] {0} 구간을 제거하면 {1}")
    @MethodSource
    @DisplayName("구간 제거")
    void remove(Section removedSection, Section expected) {
        Section section = Section.of(
            station("강남"), station("광교"), Distance.from(5));

        //when
        section.remove(removedSection);

        //then
        assertThat(section)
            .isEqualTo(expected);
    }

    @Test
    @DisplayName("null 구간을 제거")
    void remove_nullSection_thrownIllegalArgumentException() {
        //given
        Section section = Section.of(
            station("교대"), station("광교"), Distance.from(5));

        //when
        ThrowingCallable removeCall = () -> section.remove(null);

        //then
        assertThatIllegalArgumentException()
            .isThrownBy(removeCall)
            .withMessageContaining("지워지는 구간은 null 일 수 없습니다.");
    }

    @ParameterizedTest(name = "[{index}] 강남,광교 구간에서 {0} 구간을 제거할 수 없다")
    @MethodSource("sameOrNotExistStation")
    @DisplayName("모든 역이 같거나 다른 구간을 제거")
    void remove_sameOrNotExistStation_thrownIllegalArgumentException(Section removedSection) {
        Section section = Section.of(
            station("강남"), station("광교"), Distance.from(Integer.MAX_VALUE));

        //when
        ThrowingCallable removeCall = () -> section.remove(removedSection);

        //then
        assertThatExceptionOfType(InvalidDataException.class)
            .isThrownBy(removeCall)
            .withMessageEndingWith("제거할 수 없습니다.");
    }

    @Test
    @DisplayName("제거되는 구간의 길이가 더 크거나 같은 상태로 제거하면 IllegalArgumentException")
    void remove_greaterDistance_thrownIllegalArgumentException() {
        Section section = Section.of(
            station("교대"), station("광교"), Distance.from(Integer.MAX_VALUE));

        //when
        ThrowingCallable removeCall = () -> section.remove(
            Section.of(station("교대"), station("강남"), Distance.from(Integer.MAX_VALUE))
        );

        //then
        assertThatExceptionOfType(InvalidDataException.class)
            .isThrownBy(removeCall)
            .withMessageStartingWith("역과 역 사이의 거리");
    }

    private static Stream<Arguments> remove() {
        return Stream.of(
            Arguments.of(
                Section.of(station("강남"), station("양재"), Distance.from(3)),
                Section.of(station("양재"), station("광교"), Distance.from(2))
            ),
            Arguments.of(
                Section.of(station("양재"), station("광교"), Distance.from(2)),
                Section.of(station("강남"), station("양재"), Distance.from(3))
            )
        );
    }

    private static Stream<Arguments> sameOrNotExistStation() {
        return Stream.of(
            Arguments.of(Section.of(station("반포"), station("논현"), Distance.from(3))),
            Arguments.of(Section.of(station("강남"), station("광교"), Distance.from(3)))
        );
    }

    private static Stream<Arguments> instance_emptyArgument_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(null, station("광교"), Distance.from(10)),
            Arguments.of(station("강남"), null, Distance.from(10)),
            Arguments.of(station("강남"), station("광교"), null)
        );
    }

    private static Station station(String name) {
        return Station.from(Name.from(name));
    }
}
