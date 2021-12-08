package nextstep.subway.line.domain;

import nextstep.subway.exception.CannotUpdateException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionTest {
    private Station 강남역;
    private Station 양재역;
    private Station 광교역;
    private Section 신분당선_구간;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        광교역 = new Station("광교역");
        신분당선_구간 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10).getSections().get(0);
    }

    @DisplayName("상행역과 하행역 equals 테스트")
    @Test
    void equalsStation() {
        assertThat(신분당선_구간.equalsUpStation(강남역)).isTrue();
        assertThat(신분당선_구간.equalsDownStation(광교역)).isTrue();
    }

    @DisplayName("기존 길이보다 길이가 같거나 길면 추가할 수 없음")
    @Test
    void updateUpStation_Exception() {
        assertThatThrownBy(() -> {
            신분당선_구간.updateUpStation(양재역, 10);
        }).isInstanceOf(CannotUpdateException.class).hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }
}
