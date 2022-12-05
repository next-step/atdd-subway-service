package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidDataException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static nextstep.subway.line.domain.SectionFixture.*;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionsTest {

    Line line;
    Sections sections;

    @BeforeEach
    void setup() {
        line = new Line();
        sections = new Sections();
    }

    @DisplayName("상하행역이 이미 등록되었는지 확인한다.")
    @Test
    void 상하행역_등록_여부_유효성_테스트() {
        sections.add(당산_합정_구간(line));

        assertThatThrownBy(
                () -> sections.add(당산_합정_구간(line))
        ).isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("등록되지 않은 상하행역의 구간을 확인한다.")
    @Test
    void 등록되지_않은_상하행역_유효성_테스트() {
        sections.add(영등포구청_당산_구간(line));

        assertThatThrownBy(
                () -> sections.add(여의_여의나루_구간(line))
        ).isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("노선에 첫 구간 정보를 등록한다.")
    @Test
    void 첫_구간_등록_테스트() {
        sections.add(영등포구청_당산_구간(line));

        assertThat(sections.getStations()).contains(영등포구청역(), 당산역());
    }

    @DisplayName("노선에 기존상행역-신규역 구간 정보를 등록한다.")
    @Test
    void 기존상행역_신규역_구간_등록_테스트() {
        sections.add(당산_홍대입구_구간(line));

        sections.add(당산_합정_구간(line));

        List<Station> stationSubList = new ArrayList<>(Arrays.asList(당산역(), 합정역()));
        assertThat(sections.getStations()).containsAll(stationSubList);
    }

    @DisplayName("노선에 신규역-기존하행역 구간 정보를 등록한다.")
    @Test
    void 신규역_기존하행역_구간_등록_테스트() {
        sections.add(당산_홍대입구_구간(line));

        sections.add(합정_홍대입구_구간(line));

        List<Station> stationSubList = new ArrayList<>(Arrays.asList(합정역(), 홍대입구역()));
        assertThat(sections.getStations()).containsAll(stationSubList);
    }

    @DisplayName("등록된 역 정보를 정렬하여 출력한다.")
    @Test
    void 역_리스트_조회_테스트() {

        sections.add(영등포구청_당산_구간(line));
        sections.add(당산_합정_구간(line));
        sections.add(합정_홍대입구_구간(line));

        List<Station> answer = new ArrayList<>(Arrays.asList(영등포구청역(), 당산역(), 합정역(), 홍대입구역()));
        List<Station> stations = sections.getStations().stream().collect(Collectors.toList());
        assertThat(stations).isEqualTo(answer);
    }

    @DisplayName("하나 뿐인 구간을 삭제한다.")
    @Test
    void 하나_뿐인_구간_삭제_테스트() {

        sections.add(당산_합정_구간(line));

        assertThatThrownBy(
                () -> sections.removeLineStation(line, 합정역())
        ).isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("등록된 구간의 역을 삭제한다.")
    @Test
    void 역_삭제_테스트() {

        sections.add(당산_합정_구간(line));
        sections.add(합정_홍대입구_구간(line));
        sections.removeLineStation(line, 합정역());

        List<Station> answer = new ArrayList<>(Arrays.asList(당산역(), 홍대입구역()));
        List<Station> stations = sections.getStations().stream().collect(Collectors.toList());
        assertThat(stations).isEqualTo(answer);
    }

}
