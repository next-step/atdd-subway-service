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

class LineTest {
    private Line 지하철_2호선;
    private int 거리 = 10;
    private Station 강남역;
    private Station 역삼역;
    private Station 교대역;
    private Station 서초역;

    @BeforeEach
    void setUp() {
        지하철_2호선 = new Line("2호선", "bg-green-600");
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        교대역 = new Station("교대역");
        서초역 = new Station("서초역");
    }

    @Test
    @DisplayName("지하철 노선의 정보 변경 테스트")
    void updateLine() {
        지하철_2호선.update("2호선_연장", "bg-green-500");

        Assertions.assertAll(
                () -> assertThat(지하철_2호선.getColor()).isEqualTo("bg-green-500"),
                () -> assertThat(지하철_2호선.getName()).isEqualTo("2호선_연장")
        );
    }

    @Test
    @DisplayName("지하철 노선내 등록할 수 없는 구간 실패 테스트")
    void addLineSectionUnregistered() {
        지하철_2호선.addSection(강남역, 역삼역, 거리);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> 지하철_2호선.addSection(서초역, 교대역, 거리))
                .withMessageContaining(UNREGISTERED_STATION);
    }

    @Test
    @DisplayName("지하철 노선내 구간 중복 등록 실패 테스트")
    void addLineSectionAlreadyRegistered() {
        지하철_2호선.addSection(교대역, 강남역, 거리);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> 지하철_2호선.addSection(교대역, 강남역, 거리))
                .withMessageContaining(ALREADY_REGISTERED_STATION);
    }

    @Test
    @DisplayName("지하철 노선내 구간 등록 테스트")
    void addLineSection() {
        지하철_2호선.addSection(교대역, 강남역, 거리);
        Stations stations = 지하철_2호선.getStations();

        Assertions.assertAll(
                () -> assertThat(stations.getStations()).size().isEqualTo(2),
                () -> assertThat(stations.getStations()).contains(교대역, 강남역)
        );
    }

    @Test
    @DisplayName("지하철 노선내 구간 삭제시 구간이 없는 경우 실패 테스트")
    void removeLineStationMinimumSize() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> 지하철_2호선.removeLineStation(강남역))
                .withMessageContaining(NOT_FOUND_REMOVE_STATION);
    }

    @Test
    @DisplayName("지하철 노선내 구간 삭제시 구간이 없는 경우 실패 테스트")
    void removeLineStationNotMatchRemove() {
        지하철_2호선.addSection(강남역, 역삼역, 거리);
        지하철_2호선.addSection(교대역, 강남역, 거리);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> 지하철_2호선.removeLineStation(서초역))
                .withMessageContaining(NOT_MATCH_REMOVE_STATION);
    }

    @Test
    @DisplayName("지하철 노선내 구간 삭제 테스트")
    void removeLineStation() {
        지하철_2호선.addSection(강남역, 역삼역, 거리);
        지하철_2호선.addSection(교대역, 강남역, 거리);

        지하철_2호선.removeLineStation(강남역);
        Stations 지하철_2호선_삭제_결과 = 지하철_2호선.getStations();

        Assertions.assertAll(
                () -> assertThat(지하철_2호선_삭제_결과.getStations()).size().isEqualTo(2)
        );
    }

    @Test
    @DisplayName("지하철 노선 등록 후 조회시 테스트")
    void addStationSort() {
        지하철_2호선.addSection(강남역, 역삼역, 거리);
        지하철_2호선.addSection(교대역, 강남역, 거리);
        지하철_2호선.addSection(서초역, 교대역, 거리);

        Stations 지하철_2호선_조회_결과 = 지하철_2호선.getStations();

        Assertions.assertAll(
                () -> assertThat(지하철_2호선_조회_결과.getStations()).size().isEqualTo(4),
                () -> assertThat(지하철_2호선_조회_결과.getStations()).containsExactly(서초역, 교대역, 강남역, 역삼역)
        );
    }
}
