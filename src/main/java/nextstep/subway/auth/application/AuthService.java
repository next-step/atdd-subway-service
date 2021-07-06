package nextstep.subway.auth.application;

import nextstep.subway.auth.Policy.MemberPolicy;
import nextstep.subway.auth.domain.BasicMember;
import nextstep.subway.auth.domain.ChildMember;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.TeenagerMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.common.Excetion.AuthorizationException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(AuthorizationException::new);
        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return new BasicMember();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return findMemberType(member);
    }

    private LoginMember findMemberType(Member member) {
        MemberPolicy memberPolicy = MemberPolicy.getAgePolicy(member.getAge());
        if(memberPolicy.isChild()){
            return ChildMember.of(member, memberPolicy);
        }
        if(memberPolicy.isTeenager()){
            return ChildMember.of(member, memberPolicy);
        }
        return BasicMember.of(member, memberPolicy);
    }
}
