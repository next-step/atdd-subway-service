package study.unit;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("구간 관련 테스트")
@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
public class SectionTest {
    @MockBean
    private LineService lineService;

    private Line 신분당선;
    private Station 강남역;
    private Station 양재역;
    private Station 광교역;

    @BeforeEach
    void setUp() {
        강남역 = mock(Station.class);
        when(강남역.getId()).thenReturn(1L);
        when(강남역.getName()).thenReturn("강남역");

        광교역 = mock(Station.class);
        when(광교역.getId()).thenReturn(2L);
        when(광교역.getName()).thenReturn("광교역");

        양재역 = mock(Station.class);
        when(양재역.getId()).thenReturn(3L);
        when(양재역.getName()).thenReturn("양재역");

        신분당선 = new Line("신분당선", "GREEN", 강남역, 광교역, 10);
        // ReflectionTestUtils.setField(신분당선, "id", 1L);
    }

    @DisplayName("노선 구간 추가")
    @Test
    void addSection() {
        신분당선.addLineStation(강남역, 양재역, 5);

        assertThat(신분당선.getSections()).hasSize(2);
        assertThat(신분당선.getStations()).hasSize(3);
        assertThat(신분당선.getStations()).extracting("name")
                .containsExactly("강남역", "양재역", "광교역");
    }

    @DisplayName("노선 구간 삭제")
    @Test
    void removeSection() {
        신분당선.addLineStation(강남역, 양재역, 5);

        신분당선.removeLineStation(광교역);

        assertThat(신분당선.getSections()).hasSize(1);
        assertThat(신분당선.getStations()).hasSize(2);
    }
}
