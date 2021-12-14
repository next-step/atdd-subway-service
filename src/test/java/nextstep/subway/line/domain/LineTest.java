package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class LineTest {

    @Test
    @DisplayName("노선 정보를 수정한다.")
    public void update() throws Exception {
        // given
        Line line = new Line("4호선", "skyblue");
        Line updateLine = new Line("1호선", "blue");

        // when
        line.update(updateLine);

        // then
        assertThat(line.getName()).isEqualTo(updateLine.getName());
        assertThat(line.getColor()).isEqualTo(updateLine.getColor());
    }

    @Test
    @DisplayName("Line 객체를 생성한다.")
    public void of() throws Exception {
        // when
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Line result = Line.of("신분당선", "red", 강남역, 양재역, 10);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getStations()).containsExactly(강남역, 양재역);
    }

    @Test
    @DisplayName("해당 라인이 경로에 포함되면 추가 금액을 리턴한다.")
    public void getAddPriceIfContains() throws Exception {
        // given
        Sections sections = mock(Sections.class);
        Line line = new Line(900, sections);

        given(sections.isContains(anyList())).willReturn(true);

        // when
        int result = line.getAddPriceIfContains(new ArrayList<>());

        // then
        assertThat(result).isEqualTo(line.getAddPrice());
    }

    @Test
    @DisplayName("해당 라인이 경로에 포함되지 않으면 0원을 리턴한다.")
    public void getAddPriceIfContains_not_contains() throws Exception {
        // given
        Sections sections = mock(Sections.class);
        Line line = new Line(900, sections);

        given(sections.isContains(anyList())).willReturn(false);

        // when
        int result = line.getAddPriceIfContains(new ArrayList<>());

        // then
        assertThat(result).isEqualTo(0);
    }
}