package nextstep.subway.auth.application;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import nextstep.subway.auth.domain.*;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.member.domain.*;

@Service
@Transactional(readOnly = true)
public class AuthReadService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthReadService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return new LoginMember();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
