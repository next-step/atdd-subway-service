package nextstep.subway.line.domain.section;

import nextstep.subway.exception.SectionException;
import nextstep.subway.line.domain.line.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionsTest {

    private Station 강남역 = new Station("강남역");
    private Station 판교역 = new Station("판교역");
    private Station 수지역 = new Station("수지역");
    private Station 광교역 = new Station("광교역");
    private Station 홍대역 = new Station("홍대역");
    private Station 신촌역 = new Station("신촌역");
    private Line 신분당선_강남_광교 = new Line("신분당선", "red", 강남역, 광교역, 10);

    private Line 신분당선_강남_판교_수지_광교 = new Line("신분당선", "red", 강남역, 광교역, 20);

    @BeforeEach
    void set_신분당선_강남_판교_수지_광교() {
        신분당선_강남_판교_수지_광교.addSection(강남역, 판교역, 4);
        신분당선_강남_판교_수지_광교.addSection(판교역, 수지역, 7);
    }

    @Test
    public void 지하철_노선_등록() {
        신분당선_강남_광교.addSection(강남역, 판교역, 9);

        assertThat(신분당선_강남_광교.getStations()).containsExactly(강남역, 판교역, 광교역);
    }

    @Test
    @DisplayName("요청된 구간은 이미 등록 되어있음.")
    public void 지하철_노선_등록_중복_오류() {
        assertThatThrownBy(() -> {
            신분당선_강남_광교.addSection(강남역, 광교역, 10);
        }).isInstanceOf(SectionException.class);
    }

    @Test
    @DisplayName("연결 구간이 없어 오류 발생")
    public void 지하철_노선_등록_연결_오류() {
        assertThatThrownBy(() -> {
            신분당선_강남_광교.addSection(홍대역, 신촌역, 10);
        }).isInstanceOf(SectionException.class);
    }

    @Test
    @DisplayName("[상행기준] 지하철 노선의 구간을 추가한다. ([기준]강남 -> 광교) => (강남 -> 판교 -> 광교)")
    public void 상행을_기준으로_신규_노선_추가() {
        신분당선_강남_광교.addSection(강남역, 판교역, 5);
    }

    @Test
    @DisplayName("[상행기준] 지하철 노선의 구간을 추가한다. ([기준]강남 -> 광교) => (판교 -> 강남 -> 광교)")
    public void 상행을_기준으로_신규_종점_노선_추가() {
        신분당선_강남_광교.addSection(판교역, 강남역, 55);
    }

    @Test
    @DisplayName("[상행기준] 지하철 노선의 구간을 추가한다. ([기준]강남 -> 광교) => (강남 -> 판교 -> 광교)")
    public void 상행을_기준으로_신규_노선_추가_거리오류() {
        assertThatThrownBy(() -> {
            신분당선_강남_광교.addSection(강남역, 판교역, 15);
        }).isInstanceOf(SectionException.class);
    }

    @Test
    @DisplayName("[하행기준] 지하철 노선의 구간을 추가한다. (강남 -> [기준]광교) => (강남 -> 판교 -> 광교)")
    public void 하행을_기준으로_신규_노선_추가() {
        신분당선_강남_광교.addSection(강남역, 판교역, 5);
    }

    @Test
    @DisplayName("[하행기준] 지하철 노선의 구간을 추가한다. (강남 -> [기준]광교) => (강남 -> 판교 -> 광교)")
    public void 하행을_기준으로_신규_종점_노선_추가() {
        신분당선_강남_광교.addSection(판교역, 광교역, 5);
    }

    @Test
    @DisplayName("[하행기준] 지하철 노선의 구간을 추가한다. (강남 -> [기준]광교) => (강남 -> 판교 -> 광교)")
    public void 하행을_기준으로_신규_노선_추가_거리오류() {
        assertThatThrownBy(() -> {
            신분당선_강남_광교.addSection(강남역, 판교역, 15);
        }).isInstanceOf(SectionException.class);
    }

    @Test
    @DisplayName("[중간역] 지하철 구간 삭제 (강남 -> [삭제]판교 -> 수지 -> 광교)")
    public void 지하철_중간_구간_삭제() {
        신분당선_강남_판교_수지_광교.removeSection(판교역);

        assertThat(신분당선_강남_판교_수지_광교.getStations()).containsExactly(강남역, 수지역, 광교역);
    }

    @Test
    @DisplayName("[상행 종점] 지하철 구간 삭제 ([삭제]강남 -> 판교 -> 수지 -> 광교)")
    public void 지하철_상행_종점_삭제() {
        신분당선_강남_판교_수지_광교.removeSection(강남역);

        assertThat(신분당선_강남_판교_수지_광교.getStations()).containsExactly(판교역, 수지역 ,광교역);
    }

    @Test
    @DisplayName("[하행 종점] 지하철 구간 삭제 ([삭제]강남 -> 판교 -> 수지 -> 광교)")
    public void 지하철_하행_종점_삭제() {
        신분당선_강남_판교_수지_광교.removeSection(광교역);

        assertThat(신분당선_강남_판교_수지_광교.getStations()).containsExactly(강남역, 판교역, 수지역);
    }

    @Test
    @DisplayName("지하철 구간 존재 하지 않는 구간 삭제 (강남 -> 판교 -> 수지 -> 광교)")
    public void 지하철_없는_구간_삭제_오류발생() {
        assertThatThrownBy(() -> {
            신분당선_강남_판교_수지_광교.removeSection(홍대역);
        }).isInstanceOf(SectionException.class);
    }

}