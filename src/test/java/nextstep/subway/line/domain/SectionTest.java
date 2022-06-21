package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionTest {
    Station 정자역;
    Station 강남역;
    Section 정자역_강남역;
    Line 신분당선;

    @BeforeEach
    void setUp() {
        정자역 = new Station("정자역");
        강남역 = new Station("강남역");
        신분당선 = new Line("신분당선", "bg-red-600");
        정자역_강남역 = new Section(신분당선, 정자역, 강남역, 10000);
    }
    @Test
    @DisplayName("추가할 구간의 길이가 기존 역 사이 길이보다 긴 경우 에러를 발생시킨다.")
    void throwExceptionWhenConnectUpStation() {
        Station 양재역 = new Station("양재역");
        Section 정자역_양재역 = new Section(신분당선, 정자역, 양재역, 20000);

        assertThatThrownBy(() -> 정자역_강남역.connectUpStationWith(정자역_양재역))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @Test
    @DisplayName("추가할 구간의 길이가 기존 역 사이 길이보다 긴 경우 에러를 발생시킨다.")
    void throwExceptionWhenConnectDownStation() {
        Station 양재역 = new Station("양재역");
        Section 양재역_강남역 = new Section(신분당선, 양재역, 강남역, 20000);

        assertThatThrownBy(() -> 정자역_강남역.connectDownStationWith(양재역_강남역))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }
}
