package nextstep.subway.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.member.domain.Favorite;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.FavoriteResponse;
import nextstep.subway.member.domain.Favorites;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.member.exception.MemberNotFoundException;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.Path;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    //private final PathService pathService;
    private final MemberRepository memberRepository;
    //private final Favorites.FavoriteRepository favoriteRepository;

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = findMemberById(id);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

//    @Transactional
//    public FavoriteResponse addFavorite(Long id, FavoriteRequest request) {
//        Member member = findMemberById(id);
//        Favorite favorite = getFavorite(request).by(member);
//        favoriteRepository.save(favorite);
//        //member.addFavorite(favorite);
//        //memberRepository.flush();
//        return FavoriteResponse.of(favorite);
//    }
//
//
//    @Transactional
//    public void deleteFavorite(Long memberId, Long favoriteId) {
//        Member member = findMemberById(memberId);
//        member.removeFavorite(favoriteId);
//    }

    public MemberResponse findMember(Long id) {
        return MemberResponse.of(findMemberById(id));
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

//    private Favorite getFavorite(FavoriteRequest request) {
//        Path path = pathService.getShortestPath(request);
//        return Favorite.of(path);
//    }
//
//    public List<FavoriteResponse> findFavorites(Long id) {
//        Member member = findMemberById(id);
//        return FavoriteResponse.ofList(member.getFavorites());
//    }
}
