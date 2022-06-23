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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        confirmExistMyFavorite(loginMember, request);
        Member member = memberService.findMemberById(loginMember.getId());
        Station upStation = stationService.findStationById(request.getSource());
        Station downStation = stationService.findStationById(request.getTarget());
        Favorite newFavorite = favoriteRepository.save(new Favorite(member, upStation, downStation));
        return FavoriteResponse.of(newFavorite);
    }

    public void confirmExistMyFavorite(LoginMember loginMember, FavoriteRequest request) {
        if (favoriteRepository.existsByMemberIdAndUpStationIdAndDownStationId(loginMember.getId(), request.getSource(), request.getTarget())) {
            throw new IllegalArgumentException("이미 등록된 즐겨찾기 입니다.");
        }
    }

    public void deleteFavorite(Long id, LoginMember loginMember) {
        if (!favoriteRepository.existsByIdAndMemberId(id, loginMember.getId())) {
            throw new IllegalArgumentException("즐겨찾기가 존재하지 않습니다.");
        }
        favoriteRepository.deleteById(id);
    }

    public List<FavoriteResponse> getFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        return favorites.stream()
                        .map(FavoriteResponse::of)
                        .collect(Collectors.toList());
    }
}
