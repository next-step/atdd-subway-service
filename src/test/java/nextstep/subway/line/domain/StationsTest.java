package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.station.domain.Station;

class StationsTest {
    private Line 신분당선;

    private Station 강남역;
    private Station 양재역;
    private Station 판교역;
    private Station 광교역;

    private Section 강남_판교_구간;
    private Section 강남_양재_구간;
    private Section 양재_판교_구간;
    private Section 퍈교_광교_구간;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");
        광교역 = new Station("광교약");

        강남_판교_구간 = new Section(신분당선, 강남역, 판교역, 10);
        강남_양재_구간 = new Section(신분당선, 강남역, 양재역, 8);
        양재_판교_구간 = new Section(신분당선, 양재역, 판교역, 2);
        퍈교_광교_구간 = new Section(신분당선, 판교역, 광교역, 2);
    }

    @DisplayName("역이 존재하는지 확인")
    @Test
    void containsStation() {
        Stations stations = new Stations(Arrays.asList(강남역, 양재역));

        assertAll(
            () -> assertThat(stations.containsStation(강남역))
                .isTrue(),
            () -> assertThat(stations.containsStation(양재역))
                .isTrue(),
            () -> assertThat(stations.containsStation(판교역))
                .isFalse()
        );
    }

    @DisplayName("비어있지 않음 확인")
    @Test
    void isNotEmpty_true() {
        Stations stations = new Stations(Arrays.asList(강남역, 양재역));

        assertThat(stations.isNotEmpty())
            .isTrue();
    }

    @DisplayName("비어있는지 확인")
    @Test
    void isNotEmpty_false() {
        Stations stations = new Stations();

        assertThat(stations.isNotEmpty())
            .isFalse();
    }

    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가 실패")
    @Test
    void checkCanAddStation_errorWhenStationsContainsAll() {
        Stations stations = new Stations(Arrays.asList(강남역, 양재역));

        assertThatExceptionOfType(SubwayException.class)
            .isThrownBy(() -> stations.checkCanAddStation(강남_양재_구간))
            .withMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("상행역과 하행역이 모두 노선에 없다면 추가 실패")
    @Test
    void checkCanAddStation_errorWhenSectionNotInStations() {
        Stations stations = new Stations(Arrays.asList(강남역, 양재역));

        assertThatExceptionOfType(SubwayException.class)
            .isThrownBy(() -> stations.checkCanAddStation(퍈교_광교_구간))
            .withMessage("등록할 수 없는 구간 입니다.");
    }
}