package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.FavoriteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static nextstep.subway.Fixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @InjectMocks
    private FavoriteService favoriteService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private FavoriteRepository favoriteRepository;
    private Station 강남역;
    private Station 양재역;
    private Station 삼성역;
    private Station 학동역;
    private Station 천호역;
    private Station 길동역;
    private Member 회원;
    private Favorite 즐겨찾기1;
    private Favorite 즐겨찾기2;
    private Favorite 즐겨찾기3;

    @BeforeEach
    public void setUp() {
        강남역 = createStation("강남역", 1l);
        양재역 = createStation("양재역", 2l);
        삼성역 = createStation("삼성역", 3l);
        학동역 = createStation("학동역", 4l);
        천호역 = createStation("천호역", 5l);
        길동역 = createStation("길동역", 6l);
        회원 = createMember(14,"test@github.com", "test1023", 25);
    }

    @DisplayName("즐겨찾기 등록시 회원이 없으면 예외발생")
    @Test
    void returnsExceptionWithNoneMember() {
        when(memberRepository.findById(회원.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> favoriteService.createFavorite(회원.getId(), 강남역.getId(), 양재역.getId()))
                .isInstanceOf(FavoriteException.class)
                .hasMessageStartingWith("존재하지 않는 회원입니다");
    }


    @DisplayName("즐겨찾기 등록시 출발역이 없으면 예외발생")
    @Test
    void returnsExceptionWithNoneSourceStation() {
        when(memberRepository.findById(회원.getId())).thenReturn(Optional.of(회원));
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> favoriteService.createFavorite(회원.getId(), 강남역.getId(), 양재역.getId()))
                .isInstanceOf(FavoriteException.class)
                .hasMessageStartingWith("존재하지 않는 출발역입니다");
    }

    @DisplayName("즐겨찾기 등록시 도착역이 없으면 예외발생")
    @Test
    void returnsExceptionWithNoneTargetStation() {
        when(memberRepository.findById(회원.getId())).thenReturn(Optional.of(회원));
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(양재역.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> favoriteService.createFavorite(회원.getId(), 강남역.getId(), 양재역.getId()))
                .isInstanceOf(FavoriteException.class)
                .hasMessageStartingWith("존재하지 않는 도착역입니다");
    }

    @DisplayName("즐겨찾기 등록시 즐겨찾기 조회")
    @Test
    void returnsFavorite() {
        when(memberRepository.findById(회원.getId())).thenReturn(Optional.of(회원));
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(양재역.getId())).thenReturn(Optional.of(양재역));

        FavoriteResponse favorite = favoriteService.createFavorite(회원.getId(), 강남역.getId(), 양재역.getId());

        assertAll(() -> assertThat(favorite).isNotNull(),
                () -> assertThat(favorite.getSource().getName()).isEqualTo(강남역.getName()),
                () -> assertThat(favorite.getTarget().getName()).isEqualTo(양재역.getName()));
    }

    @DisplayName("즐겨찾기 등록시 이전에 저장된 회원,출발역,도착역이 존재하면 예외발생")
    @Test
    void returnsExceptionWithSameMemberAndStation() {
        즐겨찾기1 = createFavorite(회원, 강남역, 양재역);
        when(memberRepository.findById(회원.getId())).thenReturn(Optional.of(회원));
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(양재역.getId())).thenReturn(Optional.of(양재역));
        when(favoriteRepository.findByMemberAndSourceStationAndTargetStation(회원, 강남역, 양재역))
                .thenReturn(Optional.of(즐겨찾기1));

        Assertions.assertThatThrownBy(() -> favoriteService.createFavorite(회원.getId(), 강남역.getId(), 양재역.getId()))
                .isInstanceOf(FavoriteException.class)
                .hasMessageStartingWith("이미 등록된 역입니다");
    }

    @DisplayName("즐겨찾기 목록조회시 저장되있던 회원의 즐겨찾기 목록조회")
    @Test
    void returnsResponseFavorites() {
        즐겨찾기1 = createFavorite(회원, 강남역, 양재역);
        즐겨찾기2 = createFavorite(회원, 삼성역, 학동역);
        즐겨찾기3 = createFavorite(회원, 천호역, 길동역);

        when(memberRepository.findById(회원.getId())).thenReturn(Optional.of(회원));
        when(favoriteRepository.findAllByMember(회원)).thenReturn(Arrays.asList(new Favorite[]{즐겨찾기1, 즐겨찾기2, 즐겨찾기3}));

        List<FavoriteResponse> favoriteResponses = favoriteService.findAllFavoriteByMember(회원.getId());
        assertAll(() -> assertThat(favoriteResponses).hasSize(3),
                () -> assertThat(favoriteResponses.get(0).getSource().getName()).isEqualTo(강남역.getName()),
                () -> assertThat(favoriteResponses.get(0).getTarget().getName()).isEqualTo(양재역.getName()),
                () -> assertThat(favoriteResponses.get(1).getSource().getName()).isEqualTo(삼성역.getName()),
                () -> assertThat(favoriteResponses.get(1).getTarget().getName()).isEqualTo(학동역.getName()),
                () -> assertThat(favoriteResponses.get(2).getSource().getName()).isEqualTo(천호역.getName()),
                () -> assertThat(favoriteResponses.get(2).getTarget().getName()).isEqualTo(길동역.getName()));
    }

    @DisplayName("즐겨찾기 삭제시 즐겨찾기가 없는경우 예외발생")
    @Test
    void returnsExceptionWithNoFavorite() {
        즐겨찾기1 = createFavorite(회원, 강남역, 양재역);

        when(memberRepository.findById(회원.getId())).thenReturn(Optional.of(회원));
        when(favoriteRepository.findById(즐겨찾기1.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> favoriteService.deleteFavorite(즐겨찾기1.getId(),회원.getId()))
                .isInstanceOf(FavoriteException.class)
                .hasMessageStartingWith("존재하지 않는 즐겨찾기 입니다");
    }

    @DisplayName("즐겨찾기 삭제시 즐겨찾기 등록한 사람과 삭제요청한 사람이 다를경우 예외발생")
    @Test
    void returnsExceptionWithDifferMember() {
        즐겨찾기1 = createFavorite(회원, 강남역, 양재역);
        Member 새로운회원 = createMember(13,"test@test.com","aeeaf234",234);

        when(memberRepository.findById(새로운회원.getId())).thenReturn(Optional.of(새로운회원));
        when(favoriteRepository.findById(즐겨찾기1.getId())).thenReturn(Optional.of(즐겨찾기1));

        Assertions.assertThatThrownBy(() -> favoriteService.deleteFavorite(즐겨찾기1.getId(),새로운회원.getId()))
                .isInstanceOf(FavoriteException.class)
                .hasMessageStartingWith("등록한사람과 다른사람입니다");
    }
}

