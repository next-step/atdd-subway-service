package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.error.ErrorMessage;
import nextstep.subway.favorite.dto.FavoritesResponse;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public void add(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = member(loginMember);
        Station source = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_FOUND_STATION.toString()));
        Station target = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_FOUND_STATION.toString()));

        List<Favorite> favorites = favoriteRepository.findByMember(member);
        boolean isPresent = favorites.stream()
                .anyMatch(favorite -> favorite.getSource().equals(source) && favorite.getTarget().equals(target));

        if (isPresent) {
            throw new IllegalArgumentException(ErrorMessage.FAVORITE_ALREADY_ADDED.toString());
        }

        favoriteRepository.save(new Favorite(member, source, target));
    }

    public FavoritesResponse search(LoginMember loginMember) {
        Member member = member(loginMember);
        List<Favorite> favorites = getFavorites(member);

        return FavoritesResponse.of(favorites.stream().map(Favorite::toResponse).collect(Collectors.toList()));
    }

    public void remove(LoginMember loginMember, Long favoriteId) {
        member(loginMember);
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_FOUND_FAVORITE.toString()));
        favoriteRepository.delete(favorite);
    }

    private Member member(LoginMember loginMember) {
        return memberRepository.findById(loginMember.getId()).orElseThrow(IllegalArgumentException::new);
    }

    private List<Favorite> getFavorites(Member member) {
        return favoriteRepository.findByMember(member);
    }
}
