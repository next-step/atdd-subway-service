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
    @DisplayName("상행역, 하행역, 사이의 거리는 반드시 필수")
    void instance_emptyArgument_thrownIllegalArgumentException(
        Station upStation, Station downStation, Distance distance) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Section.of(upStation, downStation, distance))
            .withMessageEndingWith(" 값은 필수입니다.");
    }

    @Test
    @DisplayName("상행역과 하행역은 달라야 함")
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
    void cut(Section removedSection, Section expected) {
        Section section = Section.of(
            station("강남"), station("광교"), Distance.from(5));

        // when
        section.cut(removedSection);

        // then
        assertThat(section)
            .isEqualTo(expected);
    }

    @Test
    @DisplayName("제거하려는 구간은 필수")
    void cut_nullSection_thrownIllegalArgumentException() {
        // given
        Section section = Section.of(
            station("교대"), station("광교"), Distance.from(5));

        // when
        ThrowingCallable cutCallable = () -> section.cut(null);

        // then
        assertThatIllegalArgumentException()
            .isThrownBy(cutCallable)
            .withMessageContaining("지우려는 구간은 필수입니다.");
    }

    @ParameterizedTest(name = "[{index}] 강남,광교 구간에서 {0} 구간을 제거할 수 없다")
    @MethodSource("sameOrNotExistStation")
    @DisplayName("삭제하려는 구간의 상행역이나 하행역 중 한곳이 겹쳐야 함")
    void cut_sameOrNotExistStation_thrownIllegalArgumentException(Section removedSection) {
        Section section = Section.of(
            station("강남"), station("광교"), Distance.from(Integer.MAX_VALUE));

        // when
        ThrowingCallable cutCallable = () -> section.cut(removedSection);

        // then
        assertThatExceptionOfType(InvalidDataException.class)
            .isThrownBy(cutCallable)
            .withMessageEndingWith("제거할 수 없습니다.");
    }

    @Test
    @DisplayName("제거되는 구간의 길이가 작아야 함")
    void cut_greaterDistance_thrownIllegalArgumentException() {
        Section section = Section.of(
            station("교대"), station("광교"), Distance.from(Integer.MAX_VALUE));

        // when
        ThrowingCallable cutCallable = () -> section.cut(
            Section.of(station("교대"), station("강남"), Distance.from(Integer.MAX_VALUE))
        );

        // then
        assertThatExceptionOfType(InvalidDataException.class)
            .isThrownBy(cutCallable)
            .withMessageStartingWith("역과 역 사이의 거리");
    }

    @ParameterizedTest(name = "[{index}] 강남,광교 구간에 {0} 구간을 합치면 {1}")
    @MethodSource
    @DisplayName("구간 병합")
    void merge(Section target, Section expected) {
        Section section = Section.of(
            station("강남"), station("광교"), Distance.from(5));

        // when
        Section mergedSection = section.merge(target);

        // then
        assertThat(mergedSection)
            .isEqualTo(expected);
    }

    @Test
    @DisplayName("병합하려는 구간은 필수")
    void merge_nullSection_thrownIllegalArgumentException() {
        // given
        Section section = Section.of(
            station("강남"), station("광교"), Distance.from(5));

        // when
        ThrowingCallable mergeCall = () -> section.merge(null);

        // then
        assertThatIllegalArgumentException()
            .isThrownBy(mergeCall)
            .withMessageContaining("합쳐지는 구간은 필수입니다.");
    }

    @ParameterizedTest(name = "[{index}] 강남,광교 구간에 {0} 구간을 연결할 수 없다.")
    @MethodSource("sameOrNotExistStation")
    @DisplayName("모든 역이 같거나 다른 구간을 연결")
    void merge_sameOrNotExistStation_thrownInvalidDataException(Section mergedSection) {
        // given
        Section section = Section.of(
            station("강남"), station("광교"), Distance.from(5));

        // when
        ThrowingCallable mergeCall = () -> section.merge(mergedSection);

        // then
        assertThatExceptionOfType(InvalidDataException.class)
            .isThrownBy(mergeCall)
            .withMessageContaining("하나의 겹치는 역이 존재해야 합니다.");
    }

    private static Stream<Arguments> merge() {
        return Stream.of(
            Arguments.of(
                Section.of(station("양재"), station("강남"), Distance.from(5)),
                Section.of(station("양재"), station("광교"), Distance.from(10))
            ),
            Arguments.of(
                Section.of(station("광교"), station("정자"), Distance.from(5)),
                Section.of(station("강남"), station("정자"), Distance.from(10))
            )
        );
    }

    private static Stream<Arguments> cut() {
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
