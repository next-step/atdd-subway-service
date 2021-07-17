package nextstep.subway.line.domain;

import nextstep.subway.line.exception.BelowZeroDistanceException;
import nextstep.subway.line.exception.UnaddableSectionException;
import nextstep.subway.line.exception.UndeletableStationInSectionException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static nextstep.subway.fare.domain.Fare.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Sections 도메인 테스트")
class SectionsTest {

    private static Distance 기본_구간_거리_30 = new Distance(30);
    private static Distance 절반_구간_거리_15 = new Distance(15);

    private static Station 양평역;
    private static Station 영등포구청역;
    private static Station 영등포시장역;
    private static Station 신길역;
    private static Station 오목교역;

    private static Line 오호선;
    private static Supplier<Section> 구간_영등포구청역_신길역 = () -> new Section(1L, 오호선, 영등포구청역, 신길역, 기본_구간_거리_30);
    private static Supplier<Section> 구간_영등포구청역_영등포시장역 = () -> new Section(2L, 오호선, 영등포구청역, 영등포시장역, 절반_구간_거리_15);
    private static Supplier<Section> 구간_양평역_신길역 = () -> new Section(3L, 오호선, 양평역, 신길역, 기본_구간_거리_30);
    private static Supplier<Section> 구간_오목교역_영등포구청역 = () -> new Section(4L, 오호선, 오목교역, 영등포구청역, 기본_구간_거리_30);
    private static Supplier<Section> 구간_양평역_영등포구청역 = () -> new Section(5L, 오호선, 양평역, 영등포구청역, 기본_구간_거리_30);

    private static Sections sections = new Sections();

    @BeforeEach
    void 구간_생성() {
        양평역 = new Station(1L, "양평역");
        영등포구청역 = new Station(2L, "영등포구청역");
        영등포시장역 = new Station(3L, "영등포시장역");
        신길역 = new Station(4L, "신길역");
        오목교역 = new Station(5L, "오목교역");

        오호선 = new Line(1L, "5호선", "보라색", 영등포구청역, 신길역, 기본_구간_거리_30, DEFAULT_FARE);
        sections = new Sections();
    }

    @Test
    void add_성공_케이스1() {
        // given
        Stations stations = new Stations(asList(영등포구청역, 신길역));

        // when
        sections.add(구간_영등포구청역_신길역.get());

        // then
        assertThat(sections.size()).isEqualTo(1);
        assertThat(sections.toStations()).isEqualTo(stations);
    }

    @Test
    void add_성공_케이스2() {
        // given
        Stations stations = new Stations(asList(양평역, 영등포구청역, 영등포시장역, 신길역));

        // when
        sections.add(구간_영등포구청역_신길역.get());
        sections.add(구간_영등포구청역_영등포시장역.get());
        sections.add(구간_양평역_영등포구청역.get());

        // then
        assertThat(sections.size()).isEqualTo(3);
        assertThat(sections.toStations()).isEqualTo(stations);
    }

    @Test
    void add_예외_중복된_구간() {
        // given
        Section duplicatedSection = 구간_양평역_영등포구청역.get();

        // when
        sections.add(duplicatedSection);
        sections.add(구간_영등포구청역_신길역.get());

        // Then
        assertThatExceptionOfType(UnaddableSectionException.class)
                .isThrownBy(() -> sections.add(duplicatedSection));
    }

    @Test
    void add_예외_거리부족() {
        // given
        Section shortDistanceSection = 구간_양평역_영등포구청역.get();

        // when
        sections.add(구간_오목교역_영등포구청역.get());

        // Then
        assertThatExceptionOfType(BelowZeroDistanceException.class)
                .isThrownBy(() -> sections.add(shortDistanceSection));
    }

    @Test
    void toStations_성공() {
        // given
        Station[] expectedResult = {양평역, 영등포구청역, 영등포시장역, 신길역};

        // when
        sections.add(구간_영등포구청역_신길역.get());
        sections.add(구간_영등포구청역_영등포시장역.get());
        sections.add(구간_양평역_영등포구청역.get());

        // then
        assertThat(sections.size()).isEqualTo(3);
        assertThat(sections.toStations().get()).containsExactly(expectedResult);
    }

    @MethodSource("methodSource_deleteStation_성공")
    @ParameterizedTest
    void deleteStation_성공(Station 삭제_역, Stations expectedStations, Distance expectedDistance) {
        // given
        sections.add(구간_영등포구청역_신길역.get());
        sections.add(구간_영등포구청역_영등포시장역.get());

        // when
        sections.deleteStation(삭제_역);
        Stations actualResult = sections.toStations();

        // then
        assertThat(actualResult).isEqualTo(expectedStations);
        assertThat(sections.sumDistances()).isEqualTo(expectedDistance);
    }

    static Stream<Arguments> methodSource_deleteStation_성공() {
        return Stream.of(
                Arguments.of(영등포구청역, new Stations(asList(영등포시장역, 신길역)), 절반_구간_거리_15),
                Arguments.of(영등포시장역, new Stations(asList(영등포구청역, 신길역)), 기본_구간_거리_30),
                Arguments.of(신길역, new Stations(asList(영등포구청역, 영등포시장역)), 절반_구간_거리_15)
        );
    }


    @MethodSource("methodSource_deleteStation_예외1_하나뿐인_구간")
    @ParameterizedTest
    void deleteStation_예외1_하나뿐인_구간(Station 삭제_역) {
        // given
        sections.add(구간_영등포구청역_신길역.get());

        // when, then
        assertThatExceptionOfType(UndeletableStationInSectionException.class)
                .isThrownBy(() -> sections.deleteStation(삭제_역));
    }

    static Stream<Arguments> methodSource_deleteStation_예외1_하나뿐인_구간() {
        return Stream.of(
                Arguments.of(영등포구청역),
                Arguments.of(신길역)
        );
    }

    @MethodSource("methodSource_deleteStation_예외2_존재_하지_않는_역")
    @ParameterizedTest
    void deleteStation_예외2_존재_하지_않는_역(Station 삭제_역) {
        // given
        sections.add(구간_영등포구청역_신길역.get());
        sections.add(구간_영등포구청역_영등포시장역.get());

        // when, then
        assertThatExceptionOfType(UndeletableStationInSectionException.class)
                .isThrownBy(() -> sections.deleteStation(삭제_역));
    }

    static Stream<Arguments> methodSource_deleteStation_예외2_존재_하지_않는_역() {
        return Stream.of(
                Arguments.of(오목교역),
                Arguments.of(양평역)
        );
    }
}