package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.common.Messages.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class SectionsTest {

    private Line 지하철_2호선;
    private Station 강남역;
    private Station 역삼역;
    private Station 교대역;
    private Station 신도림역;
    private Station 대림역;

    private int 거리;
    private Section 강남_역삼_지하철_구간;
    private Section 교대_강남_지하철_구간;

    @BeforeEach
    void setUp() {
        지하철_2호선 = new Line("2호선", "bg-green-600");
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        교대역 = new Station("교대역");
        신도림역 = new Station("신도림");
        대림역 = new Station("대림");
        거리 = 10;

        강남_역삼_지하철_구간 = new Section(지하철_2호선, 강남역, 역삼역, 거리);
        교대_강남_지하철_구간 = new Section(지하철_2호선, 교대역, 강남역, 거리);
    }

    @Test
    @DisplayName("지하철 구간 중복 등록 실패 테스트")
    void addSectionsDuplicateSection() {
        Sections 지하철_구간_정보 = new Sections();
        지하철_구간_정보.addSection(강남_역삼_지하철_구간);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> 지하철_구간_정보.addSection(강남_역삼_지하철_구간))
                .withMessageContaining(ALREADY_REGISTERED_STATION);
    }

    @Test
    @DisplayName("지하철 구간 등록시 해당되지 않는 지하철 구간 실패 테스트")
    void addSectionsNotMatchStation() {
        Sections 지하철_구간_정보 = new Sections();
        지하철_구간_정보.addSection(강남_역삼_지하철_구간);

        Section 신도림_대림_지하철_구간 = new Section(지하철_2호선, 신도림역, 대림역, 거리);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> 지하철_구간_정보.addSection(신도림_대림_지하철_구간))
                .withMessageContaining(UNREGISTERED_STATION);
    }

    @Test
    @DisplayName("지하철 구간 등록 테스트")
    void addSections() {
        Sections 지하철_구간_정보 = new Sections();

        지하철_구간_정보.addSection(강남_역삼_지하철_구간);

        Assertions.assertAll(
                () -> assertThat(지하철_구간_정보.getSections()).size().isEqualTo(1),
                () -> assertThat(지하철_구간_정보.getSections()).contains(강남_역삼_지하철_구간)
        );
    }

    @Test
    @DisplayName("지하철 구간 등록시 구간 길이가 초과되는 실패 테스트")
    void addSectionsDistanceLengthError() {
        Sections 지하철_구간_정보 = new Sections();
        지하철_구간_정보.addSection(강남_역삼_지하철_구간);

        Section 강남역_교대역_지하철_구간 = new Section(지하철_2호선, 강남역, 교대역, 100);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> 지하철_구간_정보.addSection(강남역_교대역_지하철_구간))
                .withMessageContaining(DISTANCE_BETWEEN_STATION);
    }

    @Test
    @DisplayName("등록되어있는 지하철 조회 테스트")
    void getSections() {
        Sections 지하철_구간_정보 = new Sections();
        지하철_구간_정보.addSection(강남_역삼_지하철_구간);

        Stations 지하철_구간_정보_조회_결과 = 지하철_구간_정보.findStations();

        Assertions.assertAll(
                () -> assertThat(지하철_구간_정보_조회_결과.getStations()).size().isEqualTo(2),
                () -> assertThat(지하철_구간_정보_조회_결과.getStations()).contains(강남역, 역삼역)
        );
    }

    @Test
    @DisplayName("지하철 구간 삭제 테스트")
    void deleteSections() {
        Sections 지하철_구간_정보 = new Sections();
        지하철_구간_정보.addSection(강남_역삼_지하철_구간);
        지하철_구간_정보.addSection(교대_강남_지하철_구간);

        지하철_구간_정보.removeSectionByStation(강남역);
        Stations 지하철_구간_정보_조회_결과 = 지하철_구간_정보.findStations();

        Assertions.assertAll(
                () -> assertThat(지하철_구간_정보_조회_결과.getStations()).size().isEqualTo(2)
        );
    }

    @Test
    @DisplayName("지하철 구간 삭제시 조회되지 않은 지하철역 실패 테스트")
    void deleteSectionsNotMatch() {
        Sections 지하철_구간_정보 = new Sections();
        지하철_구간_정보.addSection(강남_역삼_지하철_구간);
        지하철_구간_정보.addSection(교대_강남_지하철_구간);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> 지하철_구간_정보.removeSectionByStation(신도림역))
                .withMessageContaining(NOT_MATCH_REMOVE_STATION);
    }

    @Test
    @DisplayName("지하철 구간 삭제시 조회하지 않은 지하철역 삭제 실패 테스트")
    void deleteSectionsNotFoundRemove() {
        Sections 지하철_구간_정보 = new Sections();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> 지하철_구간_정보.removeSectionByStation(신도림역))
                .withMessageContaining(NOT_FOUND_REMOVE_STATION);
    }
}
