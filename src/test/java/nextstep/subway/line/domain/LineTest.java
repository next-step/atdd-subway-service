package nextstep.subway.line.domain;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.exception.NotAcceptableApiException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {

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

    @DisplayName("노선 이름 및 색상 업데이트")
    @Test
    void update() {
        // given
        String name = "new-신분당선";
        String color = "new-red";
        Line 신규_신분당선 = new Line(name, color);

        // when
        신분당선.update(신규_신분당선);

        // then
        assertAll(
                () -> assertThat(신분당선.getName()).isEqualTo(name)
                , () -> assertThat(신분당선.getColor()).isEqualTo(color)
        );
    }

    @DisplayName("정렬된 순서로 역 목록이 반환 됨")
    @Test
    void getStations() {
        // given
        Section 신규_구간 = Section.of(신분당선, 강남역, 양재역, 5);
        구간_목록.add(신규_구간);

        // when & then
        assertThat(신분당선.getStations().getStations()).containsExactly(강남역, 양재역, 광교역);
    }

    @DisplayName("구간 추가 성공 테스트 - 상행 종점 추가")
    @Test
    void addSection_success_upStation() {
        // when
        신분당선.addSection(용산역, 강남역, 5);

        // then
        assertThat(신분당선.getStations().getStations()).containsExactly(용산역, 강남역, 광교역);
    }

    @DisplayName("구간 추가 성공 테스트 - 하행 종점 추가")
    @Test
    void addSection_success_downStation() {
        // when
        신분당선.addSection(광교역, 호매실역, 5);

        // then
        assertThat(신분당선.getStations().getStations()).containsExactly(강남역, 광교역, 호매실역);
    }

    @DisplayName("구간 추가 성공 테스트 - 역 사이에 새로운 역 추가")
    @Test
    void addSection_success_betweenStation() {
        // when
        신분당선.addSection(강남역, 양재역, 5);

        // then
        assertThat(신분당선.getStations().getStations()).containsExactly(강남역, 양재역, 광교역);
    }

    @DisplayName("구간 추가 실패 테스트 - 중복된 구간")
    @Test
    void addSection_failure_duplicateSection() {
        // when & then
        assertThatExceptionOfType(NotAcceptableApiException.class)
                .isThrownBy(() -> 신분당선.addSection(강남역, 광교역, 5))
                .withMessage(ErrorCode.DUPLICATE_SECTION.toString());
    }

    @DisplayName("구간 추가 실패 테스트 - 상행역과 하행역 둘 중 하나도 포함되어있지 않음")
    @Test
    void addSection_failure_canNotAddSection() {
        // when & then
        assertThatExceptionOfType(NotAcceptableApiException.class)
                .isThrownBy(() -> 신분당선.addSection(용산역, 양재역, 5))
                .withMessage(ErrorCode.CAN_NOT_ADD_SECTION.toString());
    }

    @DisplayName("구간 추가 실패 테스트 - 역과 역 사이에 구간을 추가할 경우 기존 거리보다 좁은 거리를 입력해야 한다.")
    @Test
    void addSection_failure_invalidSectionDistance() {
        // when & then
        assertThatExceptionOfType(NotAcceptableApiException.class)
                .isThrownBy(() -> 신분당선.addSection(강남역, 양재역, 강남역_광교역_거리))
                .withMessage(ErrorCode.INVALID_SECTION_DISTANCE.toString());
    }

    @DisplayName("구간 제거 성공 테스트 - 상행역 제거")
    @Test
    void deleteSection_success_deleteFirstStation() {
        // given
        Section 신규_구간 = Section.of(신분당선, 강남역, 양재역, 5);
        구간_목록.add(신규_구간);

        // when
        신분당선.deleteSection(강남역);

        // then
        assertThat(신분당선.getStations().getStations()).containsExactly(양재역, 광교역);
    }

    @DisplayName("구간 제거 성공 테스트 - 하행역 제거")
    @Test
    void deleteSection_success_deleteLastStation() {
        // given
        Section 신규_구간 = Section.of(신분당선, 강남역, 양재역, 5);
        구간_목록.add(신규_구간);

        // when
        신분당선.deleteSection(광교역);

        // then
        assertThat(신분당선.getStations().getStations()).containsExactly(강남역, 양재역);
    }

    @DisplayName("구간 제거 성공 테스트 - 중간역 제거")
    @Test
    void deleteSection_success_deleteBetweenStation() {
        // given
        Section 신규_구간 = Section.of(신분당선, 강남역, 양재역, 5);
        구간_목록.add(신규_구간);

        // when
        신분당선.deleteSection(양재역);

        // then
        assertThat(신분당선.getStations().getStations()).containsExactly(강남역, 광교역);
    }

    @DisplayName("구간 제거 실패 테스트 - 삭제 가능한 구간 없음")
    @Test
    void deleteSection_failure_canNotRemoveSection() {
        // when & then
        assertThatExceptionOfType(NotAcceptableApiException.class)
                .isThrownBy(() -> 신분당선.deleteSection(강남역))
                .withMessage(ErrorCode.CAN_NOT_REMOVE_SECTION.toString());
    }

    @DisplayName("구간 제거 실패 테스트 - 등록되지 않은 역")
    @Test
    void deleteSection_failure_notRegisteredStationToLine() {
        // given
        Section 신규_구간 = Section.of(신분당선, 강남역, 양재역, 5);
        구간_목록.add(신규_구간);

        // when & then
        assertThatExceptionOfType(NotAcceptableApiException.class)
                .isThrownBy(() -> 신분당선.deleteSection(용산역))
                .withMessage(ErrorCode.NOT_REGISTERED_STATION_TO_LINE.toString());
    }
}
