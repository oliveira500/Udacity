package br.org.sidia.mymovies.interfaces;

public interface AsyncTaskDelegate {
    void onPreStart();
    void onFinish(Object output);
}
