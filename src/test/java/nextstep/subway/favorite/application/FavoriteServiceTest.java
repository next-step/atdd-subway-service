package nextstep.subway.favorite.application;

import java.util.Optional;
import nextstep.subway.favorite.domain.DeleteFavoriteException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.NotFoundFavoriteException;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.NotFoundMemberException;
import nextstep.subway.station.application.StationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private StationService stationService;

    @Mock
    private MemberService memberService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    @DisplayName("내 즐겨찾기 목록 조회 실패 - 사용자 ID가 잘못 입력됨")
    @Test
    void findAllByMemberFail() {
        given(memberService.findById(any())).willThrow(NotFoundMemberException.class);
        assertThrows(NotFoundMemberException.class, () -> favoriteService.findAllByMember(1L));
    }

    @DisplayName("내 즐겨찾기 삭제 조회 실패 - 잘못된 즐겨찾기 ID 입력")
    @Test
    void deleteFail01() {
        given(favoriteRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(NotFoundFavoriteException.class, () -> favoriteService.delete(1L, 1L));
    }

    @DisplayName("내 즐겨찾기 삭제 조회 실패 - 타인의 즐겨찾기는 삭제 불가")
    @Test
    void deleteFail02() {
        // given
        Long ownerId = 1L;

        Member member = mock(Member.class);
        given(member.getId()).willReturn(ownerId);

        given(favoriteRepository.findById(any()))
            .willReturn(Optional.of( new Favorite(member, null, null)));

        // when, then
        assertThrows(DeleteFavoriteException.class, () -> favoriteService.delete(2L, 1L));
    }
}
