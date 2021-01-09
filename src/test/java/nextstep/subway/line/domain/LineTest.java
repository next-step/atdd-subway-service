package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LineTest {
    private Line line = new Line();
    Station 양재 = new Station("양재");
    Station 광교 = new Station("광교");
    Station 정자 = new Station("정자");
    Station 강남 = new Station("강남");

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        line.addSection(양재, 광교, 10);

        List<Station> stations = line.getStations();
        assertThat(stations).containsExactlyElementsOf(Arrays.asList(양재, 광교));
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSection2() {
        // when
        line.addSection(강남, 양재, 2);
        line.addSection(정자, 강남, 5);

        //then
        List<Station> stations = line.getStations();
        assertThat(stations).containsExactlyElementsOf(Arrays.asList(정자, 강남, 양재));
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSectionWithSameStation() {
        // when
        line.addSection(강남, 광교, 3);

        // then
        assertThrows(RuntimeException.class, () -> line.addSection(강남, 광교, 3));
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        // when
        line.addSection(강남, 광교, 3);

        // then
        assertThrows(RuntimeException.class, () -> line.addSection(정자, 양재, 3));
    }
}