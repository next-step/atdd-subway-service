package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTest.*;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SectionTest {

    static Section 강남_양재_구간 = spy(new Section(신분당선, 강남역, 양재역, 10));
    static Section 양재_양재시민의숲_구간 = spy(new Section(신분당선, 양재역, 양재시민의숲역, 10));
    static Section 양재시민의숲_광교_구간 = spy(new Section(신분당선, 양재시민의숲역, 광교역, 10));
    static {
        when(강남_양재_구간.getId()).thenReturn(1L);
        when(양재_양재시민의숲_구간.getId()).thenReturn(2L);
        when(양재시민의숲_광교_구간.getId()).thenReturn(3L);
    }

    @DisplayName("상행구간의 하행역은 하행구간의 상행역과 같다.")
    @Test
    void isUpwardDownwardTest() {
        assertThat(강남_양재_구간.isUpwardOf(양재_양재시민의숲_구간)).isTrue();
        assertThat(양재_양재시민의숲_구간.isUpwardOf(양재시민의숲_광교_구간)).isTrue();
        assertThat(양재_양재시민의숲_구간.isDownwardOf(강남_양재_구간)).isTrue();
        assertThat(양재시민의숲_광교_구간.isDownwardOf(양재_양재시민의숲_구간)).isTrue();
    }

    @DisplayName("구간과 구간을 상행역 기준으로 연결할 수 있다.")
    @Test
    void connectDownwardSectionTest() {
        Section 강남_광교_구간 = spy(new Section(신분당선, 강남역, 광교역, 10));
        when(강남_광교_구간.getId()).thenReturn(1L);
        Section 강남_양재_구간 = spy(new Section(신분당선, 강남역, 양재역, 5));
        when(강남_양재_구간.getId()).thenReturn(2L);

        boolean connected = 강남_광교_구간.connectIfHasEqualStation(강남_양재_구간);

        assertThat(connected).isTrue();
        assertThat(강남_광교_구간.getStations()).containsExactly(양재역, 광교역);
        assertThat(강남_광교_구간.getDistance()).isEqualTo(5);
    }

    @DisplayName("구간과 구간을 하행역 기준으로 연결할 수 있다.")
    @Test
    void connectUpwardSectionTest() {
        Section 강남_광교_구간 = spy(new Section(신분당선, 강남역, 광교역, 10));
        when(강남_광교_구간.getId()).thenReturn(1L);
        Section 양재_광교_구간 = spy(new Section(신분당선, 양재역, 광교역, 5));
        when(양재_광교_구간.getId()).thenReturn(2L);

        boolean connected = 강남_광교_구간.connectIfHasEqualStation(양재_광교_구간);

        assertThat(connected).isTrue();
        assertThat(강남_광교_구간.getStations()).containsExactly(강남역, 양재역);
        assertThat(강남_광교_구간.getDistance()).isEqualTo(5);
    }

    @DisplayName("구간과 구간의 상하행역이 겹치지 않으면 연결되지 않는다.")
    @Test
    void cannotConnectSectionTest() {
        Section 강남_광교_구간 = new Section(신분당선, 강남역, 광교역, 10);
        Section 양재_양재시민의숲_구간 = new Section(신분당선, 양재역, 양재시민의숲역, 5);

        boolean connected = 강남_광교_구간.connectIfHasEqualStation(양재_양재시민의숲_구간);

        assertThat(connected).isFalse();
    }

    @ParameterizedTest
    @DisplayName("구간을 연결할때 연결하려는 구간의 길이가 기존 구간의 길이보다 같거나 크면 연결할 수 없다.")
    @ValueSource(ints = {10, 11})
    void connectSectionOverDistanceTest(int longDistance) {
        // given
        Section 강남_양재시민의숲 = new Section(신분당선, 강남역, 양재시민의숲역, 10);
        Section 강남_양재 = new Section(신분당선, 강남역, 양재역, longDistance);

        // when then
        assertThatThrownBy(() -> 강남_양재시민의숲.connectIfHasEqualStation(강남_양재))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @Test
    @DisplayName("역을 사이로 인접한 두 구간의 연결하면 하나의 구간이 된다.")
    public void removeConnectingStationTest() {
        Section 강남_양재 = new Section(신분당선, 강남역, 양재역, 5);
        Section 양재_양재시민의숲 = new Section(신분당선, 양재역, 양재시민의숲역, 6);

        강남_양재.connectIfAdjacentByStation(양재_양재시민의숲, 양재역);

        assertThat(강남_양재.getStations()).containsExactly(강남역, 양재시민의숲역);
        assertThat(강남_양재.getDistance()).isEqualTo(11);
    }

    @Test
    @DisplayName("역을 사이에 두지 않는 두 구간은 연결할 수 없다.")
    public void removeEndStationTest() {
        Section 강남_양재 = new Section(신분당선, 강남역, 양재역, 5);
        Section 양재_양재시민의숲 = new Section(신분당선, 양재역, 양재시민의숲역, 6);

        boolean connected = 강남_양재.connectIfAdjacentByStation(양재_양재시민의숲, 강남역);

        assertThat(connected).isFalse();
    }
}