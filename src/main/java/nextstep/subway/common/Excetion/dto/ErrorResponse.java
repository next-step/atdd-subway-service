package nextstep.subway.common.Excetion.dto;

public class ErrorResponse {
    private String errorMassage;

    public ErrorResponse() {
    }

    public ErrorResponse(String errorMassage){
        this.errorMassage = errorMassage;
    }

    public static ErrorResponse of(String errorMassage){
        return new ErrorResponse((errorMassage));
    }

    public String getErrorMassage() {
        return errorMassage;
    }
}
