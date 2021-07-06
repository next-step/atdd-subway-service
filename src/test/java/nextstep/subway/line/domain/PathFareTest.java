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
    private Station 양재역 = new Station("양재역");
    private Station 청계산역 = new Station("청계산역");
    private Station 판교역 = new Station("판교역");
    private Station 정자역 = new Station("정자역");

    private Line 경강선 = new Line("신분당선", "red", 500);
    private Station 이매역 = new Station("이매역");
    private Station 광주역 = new Station("광주역");

    private Line LINE3 = new Line("3호선", "orange");
    private Station 남부터미널역 = new Station("남부터미널역");
    private Station 교대역 = new Station("교대역");

    private Line LINE2 = new Line("2호선", "green");
    private Station 서초역 = new Station("서초역");
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

    @DisplayName("요금 계산 - 추가운임이 존재하는 노선")
    @Test
    void fare_lineHasExtraFare() {
        // given
        신분당선.add(정자역, 판교역, 7);
        신분당선.add(판교역, 청계산역, 12);
        신분당선.add(청계산역, 양재역, 15);

        List<Line> lines = new ArrayList<>();
        lines.add(신분당선);

        // when
        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findPaths(정자역, 양재역);

        // then
        assertThat(path.fare(new LoginMember())).isEqualTo(2550);
        assertThat(path.fare(children)).isEqualTo(790);
        assertThat(path.fare(youth)).isEqualTo(1450);
    }

    @DisplayName("요금 계산 - 추가운임이 존재하는 노선 여러 개를 경유하는 경우")
    @Test
    void fare_multiLineHasExtraFare() {
        // given
        신분당선.add(정자역, 판교역, 7);
        경강선.add(판교역, 이매역, 5);
        경강선.add(이매역, 광주역, 12);

        List<Line> lines = new ArrayList<>();
        lines.add(신분당선);
        lines.add(경강선);

        // when
        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findPaths(정자역, 광주역);

        // then
        assertThat(path.fare(new LoginMember())).isEqualTo(2350);
        assertThat(path.fare(children)).isEqualTo(750);
        assertThat(path.fare(youth)).isEqualTo(1350);
    }
}
