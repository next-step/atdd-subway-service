package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

public class PathFinderTest {
    
    @Test
    @DisplayName("최단 경로 확인")
    void 최단_경로_확인() {
        // given
        Station 을지로4가 = Station.from("을지로4가");
        Station 동대문역사문화공원 = Station.from("동대문역사문화공원");
        Line 이호선 = Line.of("이호선", "bg-green-600", 을지로4가, 동대문역사문화공원, Distance.from(30));
        Line 오호선 = Line.of("오호선", "bg-purple-600", 을지로4가, 동대문역사문화공원, Distance.from(50));
        
        PathFinder pathFinder = PathFinder.of(Lines.of(Arrays.asList(이호선, 오호선)));
        
        // when
        Path path = pathFinder.findShortestPath(을지로4가, 동대문역사문화공원);
        
        // then
        assertThat(path.getDistance()).isEqualTo(30);
    }
    
    @Test
    @DisplayName("출발역과 도착역이 같을 수 없다")
    void 유효성_검사_같은_출발역_도착역() {
        // given
        Station 서초역 = Station.from("서초역");
        Station 교대역 = Station.from("교대역");
        Line 이호선 = Line.of("이호선", "bg-green-600", 서초역, 교대역, Distance.from(20));
        
        PathFinder pathFinder = PathFinder.of(Lines.of(Arrays.asList(이호선)));
        
        // when, then 
        assertThrows(IllegalArgumentException.class, ()->{
            pathFinder.findShortestPath(서초역, 서초역);
                });
    }
    
    @Test
    @DisplayName("출발역과 도착역은 연결되어있어야 한다")
    void 유효성_검사_출발역_도착역_연결() {
        // given
        Station 서초역 = Station.from("서초역");
        Station 교대역 = Station.from("교대역");
        Line 이호선 = Line.of("이호선", "bg-green-600", 서초역, 교대역, Distance.from(20));
        Station 이수역 = Station.from("이수역");
        Station 사당역 = Station.from("사당역");
        Line 사호선 = Line.of("사호선", "bg-blue-600", 이수역, 사당역, Distance.from(10));
        
        PathFinder pathFinder = PathFinder.of(Lines.of(Arrays.asList(이호선, 사호선)));
        
        // when, then
        assertThrows(IllegalArgumentException.class, ()->{
            pathFinder.findShortestPath(서초역, 이수역);
                });
    }
    
    @Test
    @DisplayName("존재하지 않는 출발역이나 도착역을 조회할수없다")
    void 유효성_검사_존재하지_않는_역() {
        // given
        Station 서초역 = Station.from("서초역");
        Station 교대역 = Station.from("교대역");
        Line 이호선 = Line.of("이호선", "bg-green-600", 서초역, 교대역, Distance.from(20));
        
        PathFinder pathFinder = PathFinder.of(Lines.of(Arrays.asList(이호선)));
        
        // when, then
        assertThrows(IllegalArgumentException.class, ()->{
            pathFinder.findShortestPath(서초역, Station.from("봉천역"));
                });
    }
    
    @Test
    @DisplayName("중복된 vertex를 추가해도 문제없이 잘 동작해야 한다")
    void 유효성_검사_중복된_vertex_추가() {
        // given
        Station 을지로4가 = Station.from("을지로4가");
        Station 동대문역사문화공원 = Station.from("동대문역사문화공원");
        Line 이호선 = Line.of("이호선", "bg-green-600", 을지로4가, 동대문역사문화공원, Distance.from(30));
        Line 오호선 = Line.of("오호선", "bg-purple-600", 을지로4가, 동대문역사문화공원, Distance.from(50));
        
        PathFinder pathFinder = PathFinder.of(Lines.of(Arrays.asList(이호선, 이호선, 오호선, 오호선)));
        
        // when
        Path path = pathFinder.findShortestPath(을지로4가, 동대문역사문화공원);
        
        // then
        assertThat(path.getDistance()).isEqualTo(30);
        
    }
    
    @Test
    @DisplayName("동일한 구간으로 여러 길이를 추가해도 문제없이 가장 짧은 길이가 나와야 한다")
    void 유효성_검사_동일한_구간의_다양한_edge_추가() {
        // given
        Station 서초역 = Station.from("서초역");
        Station 교대역 = Station.from("교대역");
        Line 이호선_길이_20 = Line.of("이호선", "bg-green-600", 서초역, 교대역, Distance.from(20));
        Line 이호선_길이_30 = Line.of("이호선", "bg-green-600", 서초역, 교대역, Distance.from(30));
        
        PathFinder pathFinder = PathFinder.of(Lines.of(Arrays.asList(이호선_길이_20, 이호선_길이_30)));
        
        // when
        Path path = pathFinder.findShortestPath(서초역, 교대역);
        
        // then
        assertThat(path.getDistance()).isEqualTo(20);
        
    }
}
