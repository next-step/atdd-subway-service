package nextstep.subway;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import static nextstep.subway.PageController.URIMapping.*;

@Controller
public class PageController {
    @GetMapping(value = {
            "/",
            STATION,
            LINE,
            SECTION,
            "/path",
            "/login",
            "/join",
            "/mypage",
            "/mypage/edit",
            "/favorites"}, produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "index";
    }

    public static final class URIMapping {
        public static final String LINE = "/lines";
        public static final String STATION = "/stations";
        public static final String SECTION = "/sections";

        private URIMapping() {
        }
    }
}
