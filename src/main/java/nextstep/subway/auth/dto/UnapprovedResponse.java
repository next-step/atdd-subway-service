package nextstep.subway.auth.dto;

public class UnapprovedResponse {
    private String message;

    public UnapprovedResponse() {
    }

    public UnapprovedResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
