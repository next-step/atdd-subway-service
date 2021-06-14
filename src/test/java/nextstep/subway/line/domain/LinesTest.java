package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinesTest {
    private Station 강남역 = new Station("강남역");
    private Station 양재역 = new Station("양재역");
    private Station 판교역 = new Station("판교역");
    private Station 정자역 = new Station("정자역");

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");
        정자역 = new Station("정자역");
    }

    @Test
    void findShortDistance() {
    }
}