package nextstep.subway.favorite.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
    private static String EMAIL = "jennie@email.com";
    private static String PASSWORD = "pw";
    private static int AGE = 30;
    
    
    private FavoriteService favoriteService;
    
    @Mock
    private FavoriteRepository favoriteRepository;
    
    @Mock
    private StationService stationService;
    
    @Mock
    private MemberService memberService;
    
    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationService, memberService);
        
    }
    
    @Test
    @DisplayName("다른 사용자의 즐겨찾기 삭제시 오류")
    void 다른_사용자가_즐겨찾기_삭제_불가() {
        List<Favorite> favorites = new ArrayList<Favorite>();
        favorites.add(Favorite.of(new Member(EMAIL, PASSWORD, AGE), Station.from("서초역"), Station.from("교대역")));
        when(favoriteRepository.findByMemberId(1L)).thenReturn(favorites);

        // when, then
        assertThrows(IllegalArgumentException.class, ()->{
            favoriteService.delete(1L, new LoginMember(1L, "another.email.com", 20));
                });
    }

}
