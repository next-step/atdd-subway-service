package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private Member 회원;
    private Station 강남역;
    private Station 교대역;
    private Station 양재역;
    private Line 이호선;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        회원 = 회원_생성(EMAIL, PASSWORD, AGE);
        강남역 = 지하철역_생성("강남역");
        양재역 = 지하철역_생성("양재역");
        교대역 = 지하철역_생성("교대역");
        이호선 = 지하철_노선_생성("이호선", "bg-green-600", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재역, 10);
    }


    @Test
    @DisplayName("즐겨찾기 등록")
    void createFavorite() {
        // given
        Long memberId = 1L;
        Long source = 1L;
        Long target = 2L;
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(회원));
        when(stationRepository.findById(source)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(target)).thenReturn(Optional.of(양재역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선, 신분당선));
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(즐겨찾기_생성(회원, 강남역, 양재역));

        // when
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(memberId, favoriteRequest);

        // then
        assertThat(favoriteResponse).isNotNull();
    }

    public static Favorite 즐겨찾기_생성(Member member, Station sourceStation, Station targetStation) {
        return new Favorite.Builder()
                .member(member)
                .sourceStation(sourceStation)
                .targetStation(targetStation)
                .build();
    }

    private Member 회원_생성(String email, String password, Integer age) {
        return new Member.Builder()
                .email(email)
                .password(password)
                .age(age)
                .build();
    }

    private Station 지하철역_생성(String name) {
        return new Station.Builder()
                .name(name)
                .build();
    }

    private Line 지하철_노선_생성(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line.Builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }

}