package nextstep.subway.member.application;

import nextstep.subway.exception.SubwayException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemberNotFoundException extends SubwayException {

    public MemberNotFoundException(Object arg) {
        super(arg);
    }
}
