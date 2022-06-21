package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final StationService stationService;
    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;

    @Autowired
    public FavoriteService(StationService stationService, MemberService memberService, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.findMemberById(loginMember.getId());
        Station upStation = stationService.findStationById(request.getSource());
        Station downStation = stationService.findStationById(request.getTarget());
        Favorite favorite = new Favorite(member, upStation, downStation);
        if (favoriteRepository.existsByMemberIdAndUpStationIdAndDownStationId(member.getId(), upStation.getId(), downStation.getId())) {
            throw new IllegalArgumentException("이미 등록된 즐겨찾기 입니다.");
        }
        Favorite newFavorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(newFavorite);
    }

    public void deleteFavorite(Long id, LoginMember loginMember) {
        if (!favoriteRepository.existsByIdAndMemberId(id, loginMember.getId())) {
            throw new IllegalArgumentException("즐겨찾기가 존재하지 않습니다.");
        }
        favoriteRepository.deleteById(id);
    }
}
