package nextstep.subway.station.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class StationResponsesTest {
    @DisplayName("지하철 역 응답 리스트로 변환")
    @Test
    void from() {
        StationResponses stationResponses = StationResponses.from(
            Arrays.asList(new Station("강남역"), new Station("양재역")));

        assertThat(stationResponses).isEqualTo(
            StationResponses.from(Arrays.asList(new Station("강남역"), new Station("양재역"))));
    }
}