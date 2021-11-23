package nextstep.subway.line.domain.section;

import nextstep.subway.exception.SectionException;
import nextstep.subway.exception.SectionServerException;
import nextstep.subway.line.domain.line.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionsTest {

    private Station 강남역 = new Station("강남역");
    private Station 광교역 = new Station("광교역");
    private Station 판교역 = new Station("판교역");
    private Station 홍대역 = new Station("홍대역");
    private Station 신촌역 = new Station("신촌역");
    private Line 신분당선_강남_광교 = new Line("신분당선", "red", 강남역, 광교역, 10);

    @Test
    public void 지하철_노선_등록() {
        Section 신규노선 = new Section(신분당선_강남_광교, 강남역, 판교역, 9);

        신분당선_강남_광교.addSection(신규노선);

        assertThat(신분당선_강남_광교.getSections()).contains(신규노선);
    }

    @Test
    @DisplayName("요청된 구간은 이미 등록 되어있음.")
    public void 지하철_노선_등록_중복_오류() {
        Section actual = new Section(신분당선_강남_광교, 강남역, 광교역, 10);

        assertThatThrownBy(() -> {
            신분당선_강남_광교.addSection(actual);
        }).isInstanceOf(SectionServerException.class);
    }

    @Test
    @DisplayName("연결 구간이 없어 오류 발생")
    public void 지하철_노선_등록_연결_오류() {
        Section actual = new Section(신분당선_강남_광교, 홍대역, 신촌역, 10);

        assertThatThrownBy(() -> {
            신분당선_강남_광교.addSection(actual);
        }).isInstanceOf(SectionServerException.class);
    }

    @Test
    @DisplayName("[상행기준] 지하철 노선의 구간을 추가한다. (강남 -> 광교) => ([기준]강남 -> 판교 -> 광교)")
    public void 상행을_기준으로_신규_노선_추가() {
        Section actual = new Section(신분당선_강남_광교, 강남역, 판교역, 5);

        신분당선_강남_광교.addSection(actual);
    }

    @Test
    @DisplayName("[상행기준] 지하철 노선의 구간을 추가한다. ([기준]강남 -> 광교) => (판교 -> 강남 -> 광교)")
    public void 상행을_기준으로_신규_종점_노선_추가() {
        Section actual = new Section(신분당선_강남_광교, 판교역, 강남역, 55);

        신분당선_강남_광교.addSection(actual);
    }

    @Test
    @DisplayName("[상행기준] 지하철 노선의 구간을 추가한다. (강남 -> 광교) => ([기준]강남 -> 판교 -> 광교)")
    public void 상행을_기준으로_신규_노선_추가_거리오류() {
        Section actual = new Section(신분당선_강남_광교, 강남역, 판교역, 15);

        assertThatThrownBy(() -> {
            신분당선_강남_광교.addSection(actual);
        }).isInstanceOf(SectionException.class);
    }

    @Test
    @DisplayName("[하행기준] 지하철 노선의 구간을 추가한다. (강남 -> 광교) => (강남 -> 판교 -> [기준]광교)")
    public void 하행을_기준으로_신규_노선_추가() {
        Section actual = new Section(신분당선_강남_광교, 강남역, 판교역, 5);

        신분당선_강남_광교.addSection(actual);
    }

    @Test
    @DisplayName("[하행기준] 지하철 노선의 구간을 추가한다. (강남 -> [기준]광교) => (강남 -> 판교 -> 광교)")
    public void 하행을_기준으로_신규_종점_노선_추가() {
        Section actual = new Section(신분당선_강남_광교, 판교역, 광교역, 5);

        신분당선_강남_광교.addSection(actual);
    }

    @Test
    @DisplayName("[하행기준] 지하철 노선의 구간을 추가한다. (강남 -> 광교) => (강남 -> 판교 -> [기준]광교)")
    public void 하행을_기준으로_신규_노선_추가_거리오류() {
        Section actual = new Section(신분당선_강남_광교, 강남역, 판교역, 15);

        assertThatThrownBy(() -> {
            신분당선_강남_광교.addSection(actual);
        }).isInstanceOf(SectionException.class);
    }

}