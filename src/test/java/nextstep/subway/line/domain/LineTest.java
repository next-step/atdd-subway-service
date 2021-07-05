package nextstep.subway.line.domain;

import nextstep.subway.advice.exception.SectionBadRequestException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Line 도메인 단위 테스트")
class LineTest {

    private Station 교대역;
    private Station 강남역;
    private Station 역삼역;
    private Station 잠실역;

    private int 교대역_강남역_거리;
    private int 강남역_역삼역_거리;
    private int 교대역_역삼역_거리;
    private int 강남역_잠실역_거리;

    private Line 이호선;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        잠실역 = new Station("잠실역");

        교대역_강남역_거리 = 1;
        강남역_역삼역_거리 = 3;
        교대역_역삼역_거리 = 4;
        강남역_잠실역_거리 = 10;

        이호선 = new Line("2호선", "green", 교대역, 역삼역, 교대역_역삼역_거리);
    }

    @DisplayName("역의 구간 목록을 순서대로 가져온다")
    @Test
    void getStations() {
        List<StationResponse> 구간_목록 = getStationResponse(이호선);
        List<String> 구간_역이름_목록 = 구간_역이름_목록_조회(구간_목록);

        assertThat(구간_역이름_목록).containsExactly("교대역", "역삼역");
    }

    @DisplayName("역의 구간을 추가한다 : 교대-강남을 추가하여 교대역-강남역-역삼역 순서대로 출력")
    @Test
    void addSection() {
        이호선.addSection(교대역, 강남역, 교대역_강남역_거리);

        List<StationResponse> 구간_목록 = getStationResponse(이호선);
        List<String> 구간_역이름_목록 = 구간_역이름_목록_조회(구간_목록);

        assertThat(구간_역이름_목록).containsExactly("교대역", "강남역", "역삼역");
    }

    @DisplayName("역의 구간을 추가한다 : 추가하려는 구간의 길이가 더 길면 익셉션 발생")
    @Test
    void addSectionDistanceException() {
        교대역_강남역_거리 = 10;

        assertThatThrownBy(() -> {
            이호선.addSection(교대역, 강남역, 교대역_강남역_거리);
        }).isInstanceOf(SectionBadRequestException.class);
    }

    @DisplayName("역의 구간을 추가한다 : 겹치는 역이 없으면 익셉션 발생")
    @Test
    void addSectionAllNotEqualException() {
        assertThatThrownBy(() -> {
            이호선.addSection(강남역, 잠실역, 강남역_잠실역_거리);
        }).isInstanceOf(SectionBadRequestException.class);
    }

    @DisplayName("역의 구간을 추가한다 : 이미 존재하는 구간이면 익셉션 발생")
    @Test
    void addSectionExistException() {
        assertThatThrownBy(() -> {
            이호선.addSection(교대역, 역삼역, 교대역_역삼역_거리);
        }).isInstanceOf(SectionBadRequestException.class);
    }

    @DisplayName("역을 삭제한다 : 교대역-강남역-역삼역에서 강남역을 삭제")
    @Test
    void removeStation() {
        이호선.addSection(교대역, 강남역, 교대역_강남역_거리);

        이호선.removeStation(강남역);

        List<StationResponse> 구간_목록 = getStationResponse(이호선);
        List<String> 구간_역이름_목록 = 구간_역이름_목록_조회(구간_목록);

        assertThat(구간_역이름_목록).containsExactly("교대역", "역삼역");
    }

    @DisplayName("역을 삭제한다 : 구간이 1개만 있다면 익셉션 발생")
    @Test
    void removeStationSizeException() {
        assertThatThrownBy(() -> {
            이호선.removeStation(강남역);
        }).isInstanceOf(SectionBadRequestException.class);
    }

    private List<String> 구간_역이름_목록_조회(List<StationResponse> stationResponses) {
        List<String> stationNames = stationResponses.stream()
            .map(StationResponse::getName)
            .collect(Collectors.toList());
        return stationNames;
    }

    public List<StationResponse> getStationResponse(Line line) {
        return line.getSortedStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }
}
