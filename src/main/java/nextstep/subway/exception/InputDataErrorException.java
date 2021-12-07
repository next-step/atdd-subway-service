package nextstep.subway.exception;

public class InputDataErrorException extends RuntimeException{

    private String errorMessage;
    private InputDataErrorCode inputDataErrorCode;

    public InputDataErrorException(InputDataErrorCode inputDataErrorCode) {
        this(inputDataErrorCode, inputDataErrorCode.errorMessage());
        this.inputDataErrorCode = inputDataErrorCode;
    }

    public InputDataErrorException(InputDataErrorCode inputDataErrorCode, String errorMessage) {
        super(errorMessage);
        this.inputDataErrorCode = inputDataErrorCode;
        this.errorMessage = errorMessage;
    }

}
