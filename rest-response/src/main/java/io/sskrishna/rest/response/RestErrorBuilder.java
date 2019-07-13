package io.sskrishna.rest.response;

public class RestErrorBuilder {
    private final ErrorCodeLookup errorCodeLookup;

    public RestErrorBuilder(ErrorCodeLookup errorCodeLookup) {
        this.errorCodeLookup = errorCodeLookup;
    }

    public RestError restError() {
        return new RestError(this.errorCodeLookup);
    }

    public FormError formError() {
        return new FormError(this.errorCodeLookup);
    }
}
