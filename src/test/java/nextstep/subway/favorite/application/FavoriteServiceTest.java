package nextstep.subway.favorite.application;

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

import java.util.Optional;

import static nextstep.subway.Fixture.createMember;
import static nextstep.subway.Fixture.createStation;
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

    private Station 강남역;
    private Station 양재역;
    private Member 회원;

    @BeforeEach
    public void setUp() {
        강남역 = createStation("강남역", 1l);
        양재역 = createStation("양재역", 2l);
        회원 = createMember("test@github.com", "test1023", 25);

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

    @DisplayName("즐겨찾기 등록시 이전에 저장된 회원,출발역,도착역이 같으면 예외발생")
    @Test
    void returnsExceptionWithSameMemberAndStation() {
        when(memberRepository.findById(회원.getId())).thenReturn(Optional.of(회원));
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(양재역.getId())).thenReturn(Optional.of(양재역));

        FavoriteResponse favorite = favoriteService.createFavorite(회원.getId(), 강남역.getId(), 양재역.getId());

        assertAll(() -> assertThat(favorite).isNotNull(),
                () -> assertThat(favorite.getSource().getName()).isEqualTo(강남역.getName()),
                () -> assertThat(favorite.getTarget().getName()).isEqualTo(양재역.getName()));
    }

}
