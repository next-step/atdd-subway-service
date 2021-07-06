package nextstep.subway.line.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Path;
import nextstep.subway.station.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathFareTest {

    private Line 신분당선 = new Line("신분당선", "red", 900);
    private Station 강남역 = new Station("강남역");
    private Station 양재역 = new Station("양재역");
    private Station 청계산역 = new Station("청계산역");
    private Station 판교역 = new Station("판교역");
    private Station 정자역 = new Station("정자역");

    private Line LINE3 = new Line("3호선", "orange");
    private Station 남부터미널역 = new Station("남부터미널역");
    private Station 교대역 = new Station("교대역");

    private Line LINE2 = new Line("2호선", "green");
    private Station 서초역 = new Station("서초역");
    private Station 역삼역 = new Station("역삼역");
    private Station 사당역 = new Station("사당역");

    private Line LINE4 = new Line("4호선", "blue");
    private Station 오이도역 = new Station("오이도역");

    private LoginMember children = new LoginMember(1L, "child@test.com", 6);
    private LoginMember youth = new LoginMember(2L, "youth@test.com", 13);

    @DisplayName("요금계산 - 기본 운임")
    @Test
    void fare_basic() {
        // given
        LINE3.add(남부터미널역, 교대역, 10);

        List<Line> lines = new ArrayList<>();
        lines.add(LINE3);

        // when
        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findPaths(남부터미널역, 교대역);

        // then
        assertThat(path.fare(new LoginMember())).isEqualTo(1250);
        assertThat(path.fare(children)).isEqualTo(530);
        assertThat(path.fare(youth)).isEqualTo(800);
    }

    @DisplayName("요금계산 - 거리비례 추가운임 구간 1")
    @Test
    void fare_overFare_additionalUnit1() {
        // given
        LINE3.add(남부터미널역, 교대역, 10);
        LINE2.add(교대역, 서초역, 12);
        LINE2.add(서초역, 사당역, 16);

        List<Line> lines = new ArrayList<>();
        lines.add(LINE3);
        lines.add(LINE2);

        // when
        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findPaths(남부터미널역, 사당역);

        // then
        assertThat(path.fare(new LoginMember())).isEqualTo(1750);
        assertThat(path.fare(children)).isEqualTo(630);
        assertThat(path.fare(youth)).isEqualTo(1050);
    }

    @DisplayName("요금계산 - 거리비례 추가운임 구간 2")
    @Test
    void fare_overFare_additionalUnit2() {
        // given
        LINE3.add(남부터미널역, 교대역, 10);
        LINE2.add(교대역, 서초역, 12);
        LINE2.add(서초역, 사당역, 16);
        LINE4.add(사당역, 오이도역, 32);

        List<Line> lines = new ArrayList<>();
        lines.add(LINE3);
        lines.add(LINE2);
        lines.add(LINE4);

        // when
        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findPaths(남부터미널역, 오이도역);

        // then
        assertThat(path.fare(new LoginMember())).isEqualTo(2250);
        assertThat(path.fare(children)).isEqualTo(730);
        assertThat(path.fare(youth)).isEqualTo(1300);
    }
}
