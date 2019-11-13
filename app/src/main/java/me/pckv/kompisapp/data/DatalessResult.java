package me.pckv.kompisapp.data;

public class DatalessResult {
    // hide the private constructor to limit subclass types (Success, Error)
    private DatalessResult() {
    }

    @Override
    public String toString() {
        if (this instanceof DatalessResult.Success) {
            return "Success";
        } else if (this instanceof DatalessResult.Error) {
            DatalessResult.Error error = (DatalessResult.Error) this;
            return "Error[exception=" + error.getError().toString() + "]";
        }
        return "";
    }

    // Success sub-class
    public final static class Success extends DatalessResult {

    }

    // Error sub-class
    public final static class Error extends DatalessResult {
        private Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        public Exception getError() {
            return this.error;
        }
    }
}
