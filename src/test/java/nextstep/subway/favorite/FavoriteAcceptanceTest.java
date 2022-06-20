package nextstep.subway.favorite;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    @DisplayName("즐겨찾기 생성")
    @Test
    void create() {

    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void inquiry() {

    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void delete() {

    }

    @DisplayName("로그인 하지 않은 경우 즐겨찾기 생성 불가")
    @Test
    void create_throwsException_ifMemberNotLoggedIn() {

    }

    @DisplayName("같은 출발역과 도착역으로 중복 생성 불가")
    @Test
    void create_throwsException_ifFavoriteExist() {

    }

    @DisplayName("존재하지 않는 역으로 중복 생성 불가")
    @Test
    void create_throwsException_ifStationNotExist() {

    }
}