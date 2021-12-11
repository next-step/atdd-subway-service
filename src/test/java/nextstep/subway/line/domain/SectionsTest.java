package nextstep.subway.line.domain;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.exception.NotAcceptableApiException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionsTest {

    private Station 강남역;
    private Station 광교역;
    private Station 양재역;
    private Station 용산역;
    private Station 호매실역;
    private Sections 구간_목록;
    private Line 신분당선;
    private int 강남역_광교역_거리;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        양재역 = new Station("양재역");
        용산역 = new Station("용산역");
        호매실역 = new Station("호매실역");
        강남역_광교역_거리 = 10;

        신분당선 = Line.of("신분당선", "red", 강남역, 광교역, 강남역_광교역_거리);
        구간_목록 = 신분당선.getSections();
    }

    @DisplayName("구간 추가 성공 테스트 - 상행 종점 추가")
    @Test
    void add_success_upStation() {
        // given
        Section 신규_구간 = Section.of(신분당선, 용산역, 강남역, 5);

        // when
        구간_목록.add(신규_구간);

        // then
        assertThat(신분당선.getStations().getStations()).containsExactly(용산역, 강남역, 광교역);
    }

    @DisplayName("구간 추가 성공 테스트 - 하행 종점 추가")
    @Test
    void add_success_downStation() {
        // given
        Section 신규_구간 = Section.of(신분당선, 광교역, 호매실역, 5);

        // when
        구간_목록.add(신규_구간);

        // then
        assertThat(신분당선.getStations().getStations()).containsExactly(강남역, 광교역, 호매실역);
    }

    @DisplayName("구간 추가 성공 테스트 - 역 사이에 새로운 역 추가")
    @Test
    void add_success_betweenStation() {
        // given
        Section 신규_구간 = Section.of(신분당선, 강남역, 양재역, 5);

        // when
        구간_목록.add(신규_구간);

        // then
        assertThat(신분당선.getStations().getStations()).containsExactly(강남역, 양재역, 광교역);
    }

    @DisplayName("구간 추가 실패 테스트 - 중복된 구간")
    @Test
    void add_failure_duplicateSection() {
        // given
        Section 신규_구간 = Section.of(신분당선, 강남역, 광교역, 5);

        // when & then
        assertThatExceptionOfType(NotAcceptableApiException.class)
                .isThrownBy(() -> 구간_목록.add(신규_구간))
                .withMessage(ErrorCode.DUPLICATE_SECTION.toString());
    }

    @DisplayName("구간 추가 실패 테스트 - 상행역과 하행역 둘 중 하나도 포함되어있지 않음")
    @Test
    void add_failure_canNotAddSection() {
        // given
        Section 신규_구간 = Section.of(신분당선, 용산역, 양재역, 5);

        // when & then
        assertThatExceptionOfType(NotAcceptableApiException.class)
                .isThrownBy(() -> 구간_목록.add(신규_구간))
                .withMessage(ErrorCode.CAN_NOT_ADD_SECTION.toString());
    }

    @DisplayName("구간 추가 실패 테스트 - 역과 역 사이에 구간을 추가할 경우 기존 거리보다 좁은 거리를 입력해야 한다")
    @Test
    void add_failure_invalidSectionDistance() {
        // given
        Section 신규_구간 = Section.of(신분당선, 강남역, 양재역, 강남역_광교역_거리);

        // when & then
        assertThatExceptionOfType(NotAcceptableApiException.class)
                .isThrownBy(() -> 구간_목록.add(신규_구간))
                .withMessage(ErrorCode.INVALID_SECTION_DISTANCE.toString());
    }

    @DisplayName("상행역 구간 존재 여부 true 테스트")
    @Test
    void hasSectionByUpStation_true() {
        // when & then
        assertThat(구간_목록.hasSectionByUpStation(강남역)).isTrue();
    }

    @DisplayName("상행역 구간 존재 여부 false 테스트")
    @Test
    void hasSectionByUpStation_false() {
        // when & then
        assertThat(구간_목록.hasSectionByUpStation(광교역)).isFalse();
    }

    @DisplayName("하행역 구간 존재 여부 true 테스트")
    @Test
    void hasSectionByDownStation_true() {
        // when & then
        assertThat(구간_목록.hasSectionByDownStation(광교역)).isTrue();
    }

    @DisplayName("하행역 구간 존재 여부 false 테스트")
    @Test
    void hasSectionByDownStation_false() {
        // when & then
        assertThat(구간_목록.hasSectionByDownStation(강남역)).isFalse();
    }

    @DisplayName("삭제 가능한 구간 존재 여부 true 테스트")
    @Test
    void hasDeletableSection_true() {
        // given
        Section 신규_구간 = Section.of(신분당선, 용산역, 강남역, 5);
        구간_목록.add(신규_구간);

        // when & then
        assertThat(구간_목록.hasDeletableSection()).isTrue();
    }

    @DisplayName("삭제 가능한 구간 존재 여부 false 테스트")
    @Test
    void hasDeletableSection_false() {
        // when & then
        assertThat(구간_목록.hasDeletableSection()).isFalse();
    }

    @DisplayName("상행역으로 구간 조회 true 테스트")
    @Test
    void getSectionByUpStation_true() {
        // when
        Section 구간 = 구간_목록.getSectionByUpStation(강남역);

        // then
        assertAll(
                () -> assertThat(구간.getUpStation()).isEqualTo(강남역)
                , () -> assertThat(구간.getDownStation()).isEqualTo(광교역)
                , () -> assertThat(구간.getDistance()).isEqualTo(강남역_광교역_거리)
        );
    }

    @DisplayName("상행역으로 구간 조회 false 테스트")
    @Test
    void getSectionByUpStation_false() {
        // when & then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> 구간_목록.getSectionByUpStation(광교역));
    }

    @DisplayName("하행역으로 구간 조회 true 테스트")
    @Test
    void getSectionByDownStation_true() {
        // when
        Section 구간 = 구간_목록.getSectionByDownStation(광교역);

        // then
        assertAll(
                () -> assertThat(구간.getUpStation()).isEqualTo(강남역)
                , () -> assertThat(구간.getDownStation()).isEqualTo(광교역)
                , () -> assertThat(구간.getDistance()).isEqualTo(강남역_광교역_거리)
        );
    }

    @DisplayName("하행역으로 구간 조회 false 테스트")
    @Test
    void getSectionByDownStation_false() {
        // when & then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> 구간_목록.getSectionByDownStation(강남역));
    }
}
