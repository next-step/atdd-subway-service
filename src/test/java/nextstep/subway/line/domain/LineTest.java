package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.station.domain.StationTest.지하철_생성;
import static org.assertj.core.api.Assertions.*;

public class LineTest {

    @DisplayName("노선의 이름과 색을 수정하면 정상 수정되어야 한다")
    @Test
    void lineUpdateTest() {
        // given
        Station 동대문역사문화공원역 = 지하철_생성("동대문역사문화공원역");
        Station 을지로4가역 = 지하철_생성("을지로4가역");
        Line line = 노선_생성("2호선", "bg-green-600", 동대문역사문화공원역, 을지로4가역, 10);

        // when
        Line updateLine = 노선_업데이트_생성("5호선", "bg-purple-600");
        line.update(updateLine);

        // then
        assertThat(line.getName()).isEqualTo(updateLine.getName());
        assertThat(line.getColor()).isEqualTo(updateLine.getColor());
        assertThat(노선의_지하철_이름(line)).containsExactly(동대문역사문화공원역.getName(), 을지로4가역.getName());
    }

    @DisplayName("노선에 상/하 구간을 추가하면 해당 위치에 정상 추가되어야 한다")
    @Test
    void lineAddSectionAtSideTest() {
        // given
        Station 동대문역사문화공원역 = 지하철_생성("동대문역사문화공원역");
        Station 을지로4가역 = 지하철_생성("을지로4가역");
        Station 을지로3가역 = 지하철_생성("을지로3가역");
        Station 청구역 = 지하철_생성("청구역");
        Line line2 = 노선_생성("2호선", "bg-green-600", 동대문역사문화공원역, 을지로4가역, 10);
        Line line5 = 노선_생성("5호선", "bg-purple-600", 을지로4가역, 동대문역사문화공원역, 10);

        // when
        line2.addSection(을지로3가역, 동대문역사문화공원역, 10);
        line5.addSection(동대문역사문화공원역, 청구역, 10);

        // then
        assertThat(노선의_지하철_이름(line2))
                .containsExactly(을지로3가역.getName(), 동대문역사문화공원역.getName(), 을지로4가역.getName());
        assertThat(노선의_지하철_이름(line5))
                .containsExactly(을지로4가역.getName(), 동대문역사문화공원역.getName(), 청구역.getName());
    }

    @DisplayName("노선 중간에 구간을 추가하면 해당 위치에 정상 추가되어야 한다")
    @Test
    void lineAddSectionAtMiddleTest() {
        // given
        Station 동대문역사문화공원역 = 지하철_생성("동대문역사문화공원역");
        Station 을지로3가역 = 지하철_생성("을지로3가역");
        Station 을지로4가역 = 지하철_생성("을지로4가역");
        Line line = 노선_생성("2호선", "bg-green-600", 동대문역사문화공원역, 을지로3가역, 10);

        // when
        line.addSection(동대문역사문화공원역, 을지로4가역, 5);

        // then
        assertThat(노선의_지하철_이름(line))
                .containsExactly(동대문역사문화공원역.getName(), 을지로4가역.getName(), 을지로3가역.getName());
    }

    @DisplayName("노선에 존재하지 않는 역에 구간을 추가하면 예외가 발생해야 한다")
    @Test
    void lineAddSectionNotContainStation() {
        // given
        Station 동대문역사문화공원역 = 지하철_생성("동대문역사문화공원역");
        Station 을지로3가역 = 지하철_생성("을지로3가역");
        Station 광교역 = 지하철_생성("광교역");
        Station 광교중앙역 = 지하철_생성("광교중앙역");
        Line line = 노선_생성("2호선", "bg-green-600", 동대문역사문화공원역, 을지로3가역, 10);

        // then
        assertThatThrownBy(() -> line.addSection(광교역, 광교중앙역, 5)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("2개 이상의 구간이 존재하는 노선의 특정 구간을 삭제하면 정상 삭제되고 해당 구간의 길이는 삭제된 길이만큼 늘어야 한다")
    @Test
    void lineSectionDeleteTest() {
        // given
        Station 광교역 = 지하철_생성("광교역");
        Station 광교중앙역 = 지하철_생성("광교중앙역");
        Station 상현역 = 지하철_생성("상현역");
        Station 성복역 = 지하철_생성("성복역");
        Line line = 노선_생성("신분당선", "bg-red-600", 광교역, 광교중앙역, 10);
        line.addSection(광교중앙역, 상현역, 10);
        line.addSection(상현역, 성복역, 10);

        // when
        line.removeStation(상현역);

        // then
        assertThat(노선의_지하철_이름(line)).containsExactly(광교역.getName(), 광교중앙역.getName(), 성복역.getName());
        assertThat(노선의_구간_거리(line)).containsExactly(10, 20);
    }

    @DisplayName("구간이 1개 이하인 노선의 구간을 삭제하면 예외가 발생해야 한다")
    @Test
    void lineSectionDeleteLastOne() {
        // given
        Station 광교역 = 지하철_생성("광교역");
        Station 광교중앙역 = 지하철_생성("광교중앙역");
        Line line1 = 노선_생성("신분당선", "bg-red-600", 광교역, 광교중앙역, 10);
        Line line2 = 노선_생성("분당선", "bg-yellow-600", null, null, 0);

        // then
        assertThatThrownBy(() -> line1.removeStation(광교역)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> line1.removeStation(광교중앙역)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> line2.removeStation(광교중앙역)).isInstanceOf(RuntimeException.class);
    }

    public static Line 노선_생성(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    private Line 노선_업데이트_생성(String name, String color) {
        return new Line(name, color);
    }

    private List<String> 노선의_지하철_이름(Line line) {
        return line.getStations()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }

    private List<Integer> 노선의_구간_거리(Line line) {
        List<String> lineNames = 노선의_지하철_이름(line);

        return lineNames.subList(0, lineNames.size() - 1).stream()
                .map(name -> 지하철_이름이_상행역인_구간_찾기(line, name).getDistance().getValue())
                .collect(Collectors.toList());
    }

    private Section 지하철_이름이_상행역인_구간_찾기(Line line, String name) {
        return line.getSections()
                .stream()
                .filter(it -> it.getUpStation().getName().equals(name))
                .findFirst()
                .orElseGet(() -> fail("구간을 찾을 수 없습니다."));
    }
}
