package br.org.sidia.mymovies.utils;

public class InternetException extends RuntimeException {

        private static final long serialVersionUID = -8421727263735195302L;

        public InternetException() {
            super();
        }

        public InternetException(final String detailMessage) {
            super(detailMessage);
        }

        public InternetException(final String detailMessage,
                                 final Throwable throwable) {
            super(detailMessage, throwable);
        }

        public InternetException(final Throwable throwable) {
            super(throwable);
        }
}
