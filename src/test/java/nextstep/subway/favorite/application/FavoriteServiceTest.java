package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.Favorites;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("즐겾찾기 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {


    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationService stationService;

    @Mock
    private MemberRepository memberRepository;

    private Member 회원;
    private Station 강남역;
    private Station 양재역;

    private Station 수원역;
    private Favorite 즐겨찾기_강남_양재;
    private Favorite 즐겨찾기_강남_수원;

    @BeforeEach
    void setup() {
        회원 = new Member("email", "password", 30);
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        수원역 = new Station("수원역");
        ReflectionTestUtils.setField(회원, "id", 1L);
        ReflectionTestUtils.setField(강남역, "id", 1L);
        ReflectionTestUtils.setField(양재역, "id", 2L);
        ReflectionTestUtils.setField(수원역, "id", 3L);
        즐겨찾기_강남_양재 = Favorite.of(회원, 강남역, 양재역);
        즐겨찾기_강남_수원 = Favorite.of(회원, 강남역, 수원역);
        ReflectionTestUtils.setField(즐겨찾기_강남_양재, "id", 1L);
        ReflectionTestUtils.setField(즐겨찾기_강남_수원, "id", 2L);
    }

    @DisplayName("즐겨찾기_등록")
    @Test
    void create_test() {
        // given
        given(stationService.findStationById(any())).willReturn(강남역);
        given(stationService.findStationById(any())).willReturn(양재역);
        given(memberRepository.findById(any())).willReturn(Optional.of(회원));
        given(favoriteRepository.save(any())).willReturn(즐겨찾기_강남_양재);
        // when
        FavoriteResponse favoriteResponse = favoriteService
                .create(회원.getId(), FavoriteRequest.of(강남역.getId(), 양재역.getId()));
        // then
        assertAll(
                () -> assertThat(favoriteResponse.getId()).isNotNull(),
                () -> assertThat(favoriteResponse.getSource().getName()).isEqualTo("강남역"),
                () -> assertThat(favoriteResponse.getTarget().getName()).isEqualTo("양재역")
        );
    }
    @DisplayName("즐겨찾기_조회")
    @Test
    void findAll_test() {
        // given
        given(favoriteRepository.findAllByMemberId(any())).willReturn(Arrays.asList(
                즐겨찾기_강남_양재, 즐겨찾기_강남_수원
        ));
        // when
        List<FavoriteResponse> 즐겨찾기_목록 = favoriteService.findAll(회원.getId());
        // then
        assertThat(
                즐겨찾기_목록.stream().map(FavoriteResponse::getId).collect(Collectors.toList())
        ).containsExactly(즐겨찾기_강남_양재.getId(),즐겨찾기_강남_수원.getId());
    }

    @DisplayName("즐겨찾기_삭제")
    @Test
    void delete_test() {
        // given
        given(favoriteRepository.findByIdAndMemberId(1L, 1L)).willReturn(Optional.of(즐겨찾기_강남_양재));
        // when
        favoriteService.remove(1L, 1L);
        // then
        verify(favoriteRepository, times(1)).delete(즐겨찾기_강남_양재);
    }

}
