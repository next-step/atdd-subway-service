package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.Excetion.MemberNotFoundException;
import nextstep.subway.common.Excetion.StationNotFoundException;
import nextstep.subway.favorite.domain.FavoriteSections;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteService {
    private StationRepository stationRepository;
    private MemberRepository memberRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public List<FavoriteResponse> findFavoriteSection(LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(() -> new MemberNotFoundException());
        FavoriteSections favoriteSections = member.getFavoriteSections();
        return favoriteSections.getFavoriteSections().stream()
                .map(favoriteSection -> FavoriteResponse.of(favoriteSection))
                .collect(Collectors.toList());
    }

    public void addFavoriteSection(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Station sourceStation = findStationById(favoriteRequest.getSourceId());
        Station targetStation = findStationById(favoriteRequest.getTargetId());
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(() -> new MemberNotFoundException());
        member.addFavoriteSection(sourceStation, targetStation);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new StationNotFoundException());
    }
}
