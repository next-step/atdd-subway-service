package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.AlreadyExistFavoritException;
import nextstep.subway.exception.NoFavoriteException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {
    private StationService stationService;
    private MemberService memberService;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, MemberService memberService, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Station sourceStation = stationService.findStationById(favoriteRequest.getSource());
        Station targetStation = stationService.findStationById(favoriteRequest.getTarget());
        Member member = memberService.findMemberById(loginMember.getId());
        Favorite favorite = new Favorite(member, sourceStation, targetStation);

        alreadyExistCheck(member, sourceStation, targetStation);

        return FavoriteResponse.of(favoriteRepository.save(favorite));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> list() {
        return FavoriteResponse.ofList(favoriteRepository.findAll());
    }

    public void deleteFavorite(Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow( () -> new NoFavoriteException("등록되지 않은 즐겨찾기 입니다."));
        favoriteRepository.deleteById(id);
    }

    private void alreadyExistCheck(Member member, Station sourceStation, Station targetStation) {
        if (favoriteRepository.existsByMemberAndSourceStationAndTargetStation(member, sourceStation, targetStation)) {
            throw new AlreadyExistFavoritException("이미 등록된 즐겨찾기 입니다.");
        }
    }
}
