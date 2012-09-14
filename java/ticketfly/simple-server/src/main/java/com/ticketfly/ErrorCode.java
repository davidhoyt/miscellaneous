package com.ticketfly;

/**
 * Encapsulates an error code and associated message.
 *
 * WARNING: This class is not presently internationalized and is
 *          therefore not localizable. Were that necessary,
 *          the use of an enum would likely have been avoided.
 *
 * @author David Hoyt &lt;dhoyt@hoytsoft.org&gt;
 */
public enum ErrorCode {
      ERROR_MISSING_INPUT           (-100, "No input was provided.")
    , ERROR_INVALID_INPUT_FORMAT    (-101, "The provided input is not formatted correctly. Valid characters include: letters, numbers, and underscores.")
    , ERROR_INVALID_SEQUENCE_NUMBER (-102, "Invalid sequence number. Numbers must be a valid integer 1 or higher.")
    ;

    private final int code;
    private final String message;
    private final String cached_output_message;

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
        this.cached_output_message = String.format("%s %d\n", message, code);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getOutputMessage() {
        return cached_output_message;
    }

    @Override
    public String toString() {
        return cached_output_message;
    }
}
