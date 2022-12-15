package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.fixture.MemberFixture;
import nextstep.subway.fixture.StationFixture;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private StationService stationService;

    @InjectMocks
    private FavoriteService favoriteService;

    private Member 회원;
    private LoginMember 로그인된_회원;
    private Station 강남역;
    private Station 삼성역;
    private Favorite 강남역_삼성역_즐겨찾기;

    @BeforeEach
    void setUp() {
        this.회원 = MemberFixture.회원;
        this.로그인된_회원 = new LoginMember(this.회원.getId(), this.회원.getEmail(), this.회원.getAge());
        this.강남역 = StationFixture.강남역;
        this.삼성역 = StationFixture.삼성역;
        this.강남역_삼성역_즐겨찾기 = new Favorite(this.회원, this.강남역, this.삼성역);

        given(memberService.findMemberByLoginMember(any())).willReturn(회원);
    }

    @DisplayName("로그인 된 회원과 구간 정보가 주어지면 즐겨찾기 생성시 즐겨찾기의 ID를 반환한다")
    @Test
    void create_favorite() {
        // given
        long 강남역_아이디 = 1L;
        long 삼성역_아이디 = 2L;
        given(stationService.findStationById(강남역_아이디)).willReturn(강남역);
        given(stationService.findStationById(삼성역_아이디)).willReturn(삼성역);
        given(favoriteRepository.save(any())).willReturn(강남역_삼성역_즐겨찾기);

        // when
        Long favoriteId = favoriteService.createFavorite(로그인된_회원, new FavoriteCreateRequest(강남역_아이디, 삼성역_아이디));

        // then
        즐겨찾기_생성됨(favoriteId);
    }

    @DisplayName("로그인 된 회원과 구간 정보가 주어지면 즐겨찾기 생성시 즐겨찾기의 ID를 반환한다")
    @Test
    void create_favorite_with_same_stations() {
        // given
        long 강남역_아이디 = 1L;
        long 삼성역_아이디 = 2L;
        given(stationService.findStationById(강남역_아이디)).willReturn(강남역);
        given(stationService.findStationById(삼성역_아이디)).willReturn(삼성역);
        given(favoriteRepository.save(any())).willReturn(강남역_삼성역_즐겨찾기);

        // when
        favoriteService.createFavorite(로그인된_회원, new FavoriteCreateRequest(강남역_아이디, 삼성역_아이디));

        // then
        즐겨찾기_생성됨(null);
    }

    @DisplayName("로그인 된 회원 정보가 주어지면 해당 회원이 등록한 즐겨찾기 목록을 응답한다")
    @Test
    void find_all_favorites_by_login_member() {
        // given
        given(favoriteRepository.findAllWithStationsByMember(회원)).willReturn(Arrays.asList(강남역_삼성역_즐겨찾기));

        // when
        List<FavoriteResponse> responses = favoriteService.findAllFavoritesBy(로그인된_회원);

        // then
        즐겨찾기_목록_조회_성공(responses);
    }

    @DisplayName("로그인 된 회원 정보와 즐겨찾기 ID가 주어지면 ID에 해당되는 즐겨찾기를 삭제한다")
    @Test
    void delete_favorite() {
        // given
        long 즐겨찾기_아이디 = 1L;
        given(favoriteRepository.findByIdAndMember(즐겨찾기_아이디, 회원)).willReturn(Optional.of(강남역_삼성역_즐겨찾기));

        // when
        favoriteService.deleteFavorite(로그인된_회원, 즐겨찾기_아이디);

        // then
        즐겨찾기_삭제됨(즐겨찾기_아이디);
    }

    @DisplayName("다른 회원의 즐겨찾기가 주어지면 즐겨찾기 삭제시 예외처리되어 요청에 실패한다")
    @Test
    void delete_with_other_member_favorite() {
        // given
        long 다른회원_즐겨찾기_아이디 = 1L;
        given(favoriteRepository.findByIdAndMember(다른회원_즐겨찾기_아이디, 회원)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> favoriteService.deleteFavorite(로그인된_회원, 다른회원_즐겨찾기_아이디))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private void 즐겨찾기_생성됨(Long favoriteId) {
        then(memberService).should(times(1)).findMemberByLoginMember(로그인된_회원);
        then(stationService).should(times(2)).findStationById(any());
        then(favoriteRepository).should(times(1)).save(any());
        assertThat(favoriteId).isEqualTo(강남역_삼성역_즐겨찾기.getId());
    }

    private void 즐겨찾기_목록_조회_성공(List<FavoriteResponse> responses) {
        then(memberService).should(times(1)).findMemberByLoginMember(로그인된_회원);
        then(favoriteRepository).should(times(1)).findAllWithStationsByMember(회원);
        assertThat(responses).hasSize(1);
    }

    private void 즐겨찾기_삭제됨(long favoriteId) {
        then(memberService).should(times(1)).findMemberByLoginMember(로그인된_회원);
        then(favoriteRepository).should(times(1)).findByIdAndMember(favoriteId, 회원);
        then(favoriteRepository).should(times(1)).delete(강남역_삼성역_즐겨찾기);
    }
}
