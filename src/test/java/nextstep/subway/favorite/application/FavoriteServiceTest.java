package nextstep.subway.favorite.application;

import nextstep.subway.ServiceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("FavoriteService 클래스 테스트")
public class FavoriteServiceTest extends ServiceTest {

    private StationResponse 강남역;
    private StationResponse 역삼역;
    private LoginMember 로그인된_사용자;

    private FavoriteService service = new FavoriteService();

    @BeforeEach
    public void setUp(@Autowired StationService stationService, @Autowired MemberService memberService) {
        super.setUp();
        강남역 = stationService.saveStation(new StationRequest("강남역"));
        역삼역 = stationService.saveStation(new StationRequest("역삼역"));

        MemberResponse 사용자 = memberService.createMember(new MemberRequest("heowc1992@gmail.com", "password", 31));
        로그인된_사용자 = new LoginMember(사용자.getId(), 사용자.getEmail(), 사용자.getAge());
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void create() {
        FavoriteRequest request = new FavoriteRequest(강남역.getId(), 역삼역.getId());

        FavoriteResponse response = service.create(로그인된_사용자, request);

        assertThat(response.getSource().getName()).isEqualTo(강남역.getName());
        assertThat(response.getTarget().getName()).isEqualTo(역삼역.getName());
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void findAll() {
        FavoriteRequest request = new FavoriteRequest(강남역.getId(), 역삼역.getId());
        service.create(로그인된_사용자, request);

        List<FavoriteResponse> favoriteResponses = service.findAll(로그인된_사용자);

        assertThat(favoriteResponses).hasSize(1);
    }

    @DisplayName("즐겨찾기 목록을 제거한다.")
    @Test
    void deleteById() {
        FavoriteRequest request = new FavoriteRequest(강남역.getId(), 역삼역.getId());
        service.create(로그인된_사용자, request);

        service.deleteById(로그인된_사용자, 강남역.getId());

        List<FavoriteResponse> favoriteResponses = service.findAll(로그인된_사용자);
        assertThat(favoriteResponses).hasSize(0);
    }
}
