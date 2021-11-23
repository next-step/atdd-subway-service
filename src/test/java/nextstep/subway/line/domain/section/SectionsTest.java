package nextstep.subway.line.domain.section;

import nextstep.subway.exception.SectionException;
import nextstep.subway.line.domain.line.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionsTest {

    private Station 강남역 = new Station("강남역");
    private Station 광교역 = new Station("광교역");
    private Line 신분당선_강남_광교 = new Line("신분당선", "red", 강남역, 광교역, 10);

    @Test
    public void 지하철_노선_등록() {
        Station 강남역 = new Station("강남역");
        Station 판교역 = new Station("판교역");
        Section 신규노선 = new Section(신분당선_강남_광교, 강남역, 판교역, 10);

        신분당선_강남_광교.addSection(신규노선);

        assertThat(신분당선_강남_광교.getSections()).contains(신규노선);
    }

    @Test
    public void 지하철_노선_등록_중복_오류() {
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");

        Section actual = new Section(신분당선_강남_광교, 강남역, 광교역, 10);

        assertThatThrownBy(() -> {
            신분당선_강남_광교.addSection(actual);
        }).isInstanceOf(SectionException.class);
    }
}