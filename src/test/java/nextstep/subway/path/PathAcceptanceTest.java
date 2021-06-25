package nextstep.subway.path;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp(){
        super.setUp();

        //지하철역 등록되어 있음

        //지하철 노선 등록되어 있음

        //지하철 노선에 지하철역 등록되어 있음
    }

    @DisplayName("최단경로를 조회한다")
    @Test
    void 최단경로_조회(){
        //when : 최단 경로 조회 요청
        //then : 최단 경로 지하철 목록이 반환됨
        //when : (출발역과 도착역이 같은 경우) 최단 경로 조회 요청
        //then : 최단 경로 조회 실패함
        //when : (출발역과 도착역이 연결되어 있지 않은 경우) 최단 경로 조회 요청
        //then : 최단 경로 조회 실패함
        //when : (출발역이나 도착역이 존재하지 않는 경우) 최단 경로 조회 요청
        //then : 최단 경로 조회 실패함
    }
    
    
}
