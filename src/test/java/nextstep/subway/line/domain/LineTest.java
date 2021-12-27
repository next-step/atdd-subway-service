package nextstep.subway.line.domain;

import nextstep.subway.line.domain.fixture.LineFixture;
import nextstep.subway.line.domain.fixture.SectionFixture;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.fixture.StationFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class LineTest {

    @BeforeEach
    void setUp() {
        LineFixture.신분당선.getSections().clear();
    }

    @DisplayName("지하철 역 정보 가져오기")
    @Test
    void 지하철_역_정보_가져오기() {
        // given
        Line 신분당선 = LineFixture.신분당선;
        지하철_노선에_역을_등록(신분당선);

        // when
        List<Station> expectedStations = 신분당선.getStations();
        List<Station> actualStations = Arrays.asList(
                StationFixture.강남역,
                StationFixture.양재역,
                StationFixture.양재시민의숲역,
                StationFixture.청계산입구역,
                StationFixture.판교역,
                StationFixture.정자역,
                StationFixture.미금역,
                StationFixture.동천역,
                StationFixture.광교역
        );

        // then
        지하철_노선에_역이_정렬됨(expectedStations, actualStations);
    }

    @DisplayName("지하철 노선 구간 추가하기")
    @Test
    void 지하철_노선_구간_추가하기() {
        // given
        Line 신분당선 = LineFixture.신분당선;

        // when
        신분당선.addToLineStation(StationFixture.강남역, StationFixture.광교역, 30);
        신분당선.addToLineStation(StationFixture.강남역, StationFixture.양재역, 4);
        신분당선.addToLineStation(StationFixture.미금역, StationFixture.광교역, 13);
        신분당선.addToLineStation(StationFixture.정자역, StationFixture.미금역, 3);

        // then
        List<Station> expectedStations = 신분당선.getStations();
        List<Station> actualStations = Arrays.asList(
                StationFixture.강남역,
                StationFixture.양재역,
                StationFixture.정자역,
                StationFixture.미금역,
                StationFixture.광교역
        );

        지하철_노선에_역이_정렬됨(expectedStations, actualStations);
    }

    @DisplayName("지하철 노선 구간 추가하기 - 예외 발생: 이미 등록된 구간")
    @Test
    void 지하철_노선_구간_추가하기_이미_등록된_구간() {
        // given
        Line 신분당선 = LineFixture.신분당선;

        // when
        신분당선.addToLineStation(StationFixture.강남역, StationFixture.광교역, 30);

        // then
        Assertions.assertThatThrownBy(() -> {
            신분당선.addToLineStation(StationFixture.강남역, StationFixture.광교역, 30);
        }).isInstanceOf(RuntimeException.class).hasMessageContaining("이미 등록된 구간 입니다.");
    }

    @DisplayName("지하철 노선 구간 추가하기 - 예외 발생: 등록할 수 없는 구간")
    @Test
    void 지하철_노선_구간_추가하기_등록할_수_없는_구간() {
        // given
        Line 신분당선 = LineFixture.신분당선;

        // when
        신분당선.addToLineStation(StationFixture.강남역, StationFixture.광교역, 30);

        // then
        Assertions.assertThatThrownBy(() -> {
            신분당선.addToLineStation(StationFixture.정자역, StationFixture.미금역, 10);
        }).isInstanceOf(RuntimeException.class).hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("지하철 노선 구간 추가하기 - 예외 발생: 기존에 등록되어 있는 구간 길이 초과")
    @Test
    void 지하철_노선_구간_추가하기_기존에_등록되어_있는_구간_길이_초과() {
        // given
        Line 신분당선 = LineFixture.신분당선;

        // when
        신분당선.addToLineStation(StationFixture.강남역, StationFixture.광교역, 10);

        // then
        Assertions.assertThatThrownBy(() -> {
            신분당선.addToLineStation(StationFixture.강남역, StationFixture.양재역, 11);
        }).isInstanceOf(RuntimeException.class).hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("지하철 노선 구간 제거하기")
    @Test
    void 지하철_노선_구간_제거하기() {
        // given
        Line 신분당선 = LineFixture.신분당선;
        지하철_노선에_역을_등록(신분당선);

        // when
        신분당선.removeLineStation(StationFixture.강남역);

        // then
        List<Station> expectedStations = 신분당선.getStations();
        List<Station> actualStations = Arrays.asList(
                StationFixture.양재역,
                StationFixture.양재시민의숲역,
                StationFixture.청계산입구역,
                StationFixture.판교역,
                StationFixture.정자역,
                StationFixture.미금역,
                StationFixture.동천역,
                StationFixture.광교역
        );

        지하철_노선에_역이_정렬됨(expectedStations, actualStations);
    }

    void 지하철_노선에_역을_등록(Line line) {
        line.getSections().add(SectionFixture.동천역_광교역);
        line.getSections().add(SectionFixture.강남역_양재역);
        line.getSections().add(SectionFixture.양재시민의숲역_청계산입구역);
        line.getSections().add(SectionFixture.미금역_동천역);
        line.getSections().add(SectionFixture.양재역_양재시민의숲역);
        line.getSections().add(SectionFixture.정자역_미금역);
        line.getSections().add(SectionFixture.청계산입구역_판교역);
        line.getSections().add(SectionFixture.판교역_정자역);
    }

    void 지하철_노선에_역이_정렬됨(List<Station> expectedStations, List<Station> actualStations) {

        List<String> expectedStationNames = expectedStations.stream().map(Station::getName).collect(Collectors.toList());
        List<String> actualStationNames = actualStations.stream().map(Station::getName).collect(Collectors.toList());

        Assertions.assertThat(expectedStationNames).containsExactlyElementsOf(actualStationNames);
    }
}
