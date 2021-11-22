package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.List;
import java.util.stream.Stream;
import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("구간들")
class SectionsTest {

    private Section 양재_광교_구간;

    @BeforeEach
    void setUp() {
        양재_광교_구간 = Section.of(
            station("양재"),
            station("광교"),
            Distance.from(Integer.MAX_VALUE));
    }

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Sections.from(양재_광교_구간));
    }

    @Test
    @DisplayName("구간이 null인 상태로 객체화")
    void instance_nullSection_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Sections.from(null))
            .withMessage("초기 구간은 반드시 존재해야 합니다.");
    }

    @Test
    @DisplayName("순서대로 정렬된 역들")
    void stations() {
        // when
        List<Station> stations = Sections.from(양재_광교_구간)
            .stations();

        // then
        assertThat(stations)
            .hasSize(2)
            .doesNotHaveDuplicates()
            .containsExactly(station("양재"), station("광교"));
    }

    @ParameterizedTest(name = "[{index}] 양재 광교 구간에 {0} 구간을 추가하면 {1} 역들")
    @DisplayName("구간 추가")
    @MethodSource
    void addSection(Section section, Station... expectedStations) {
        // given
        Sections sections = Sections.from(양재_광교_구간);

        // when
        sections.add(section);

        // then
        Assertions.assertThat(sections.stations())
            .hasSize(3)
            .doesNotHaveDuplicates()
            .containsExactly(expectedStations);
    }

    @Test
    @DisplayName("이미 존재하는 역들의 구간 추가")
    void add_duplicateStation_thrownDuplicateDataException() {
        // given
        Sections sections = Sections.from(양재_광교_구간);

        // when
        ThrowingCallable addCall = () -> sections.add(양재_광교_구간);

        // then
        assertThatExceptionOfType(DuplicateDataException.class)
            .isThrownBy(addCall)
            .withMessageEndingWith("이미 등록된 구간 입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 역들의 구간 추가")
    void add_notExistsStation_thrownNotFoundException() {
        // when
        Section 강남_정자_구간 = Section.of(
            station("강남"),
            station("정자"),
            Distance.from(5));

        Sections sections = Sections.from(양재_광교_구간);

        //when
        ThrowingCallable addCall = () -> sections.add(강남_정자_구간);

        //then
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(addCall)
            .withMessageEndingWith("등록할 수 없는 구간 입니다.");
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

    private static Station station(String name) {
        return Station.from(Name.from(name));
    }

}
