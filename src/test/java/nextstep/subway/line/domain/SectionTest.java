package nextstep.subway.line.domain;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.exception.NotAcceptableApiException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class SectionTest {

    private Station 강남역;
    private Station 광교역;
    private Station 양재역;
    private Section 구간;
    private Line 신분당선;
    private int 강남역_광교역_거리;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        양재역 = new Station("양재역");
        강남역_광교역_거리 = 10;

        신분당선 = Line.of("신분당선", "red", 강남역, 광교역, 강남역_광교역_거리);
        구간 = 신분당선.getSections().getFirstSection();
    }

    @DisplayName("구간 상행역 업데이트 성공 테스트")
    @Test
    void updateUpStation_success() {
        // when
        구간.updateUpStation(양재역, 5);

        // then
        assertThat(구간.getUpStation()).isEqualTo(양재역);
    }

    @DisplayName("구간 상행역 업데이트 실패 테스트")
    @Test
    void updateUpStation_failure() {
        // when & then
        assertThatExceptionOfType(NotAcceptableApiException.class)
                .isThrownBy(() -> 구간.updateUpStation(양재역, 강남역_광교역_거리))
                .withMessage(ErrorCode.INVALID_SECTION_DISTANCE.toString());
    }

    @DisplayName("구간 하행역 업데이트 성공 테스트")
    @Test
    void updateDownStation_success() {
        // when
        구간.updateDownStation(양재역, 5);

        // then
        assertThat(구간.getDownStation()).isEqualTo(양재역);
    }

    @DisplayName("구간 하행역 업데이트 실패 테스트")
    @Test
    void updateDownStation_failure() {
        // when & then
        assertThatExceptionOfType(NotAcceptableApiException.class)
                .isThrownBy(() -> 구간.updateDownStation(양재역, 강남역_광교역_거리))
                .withMessage(ErrorCode.INVALID_SECTION_DISTANCE.toString());
    }

    @DisplayName("구간 상행역 동등성 성공 테스트")
    @Test
    void isEqualUpStation_success() {
        // when & then
        assertThat(구간.isEqualUpStation(강남역)).isTrue();
    }

    @DisplayName("구간 상행역 동등성 실패 테스트")
    @Test
    void isEqualUpStation_failure() {
        // when & then
        assertThat(구간.isEqualUpStation(광교역)).isFalse();
    }

    @DisplayName("구간 하행역 동등성 성공 테스트")
    @Test
    void isEqualDownStation_success() {
        // when & then
        assertThat(구간.isEqualDownStation(광교역)).isTrue();
    }

    @DisplayName("구간 하행역 동등성 실패 테스트")
    @Test
    void isEqualDownStation_failure() {
        // when & then
        assertThat(구간.isEqualDownStation(강남역)).isFalse();
    }

    @DisplayName("상행역 구간 삭제 테스트")
    @Test
    void deleteByUpStation() {
        // given
        Section 신규_구간 = Section.of(신분당선, 강남역, 양재역, 5);
        신분당선.getSections().addSection(신규_구간);

        // when
        신분당선.getSections().deleteByUpStation(강남역);

        // then
        assertThat(신분당선.getStations().getStations()).containsExactly(양재역, 광교역);
    }


    @DisplayName("하행역 구간 삭제 테스트")
    @Test
    void deleteByDownStation() {
        // given
        Section 신규_구간 = Section.of(신분당선, 강남역, 양재역, 5);
        신분당선.getSections().addSection(신규_구간);

        // when
        신분당선.getSections().deleteByDownStation(광교역);

        // then
        assertThat(신분당선.getStations().getStations()).containsExactly(강남역, 양재역);
    }

    @DisplayName("중간역 구간 삭제 테스트")
    @Test
    void deleteByBetweenStation() {
        // given
        Section 신규_구간 = Section.of(신분당선, 강남역, 양재역, 5);
        신분당선.getSections().addSection(신규_구간);

        // when
        신분당선.getSections().deleteByBetweenStation(양재역);

        // then
        assertThat(신분당선.getStations().getStations()).containsExactly(강남역, 광교역);
    }

    @DisplayName("첫번째 구간 조회 테스트")
    @Test
    void getFirstSection() {
        // when
        Section 첫번째_구간 = 신분당선.getSections().getFirstSection();

        // then
        assertThat(첫번째_구간).isEqualTo(구간);
    }
}
