package br.org.sidia.mymovies.model;

import java.util.List;

public class PageResult {
    private List<Movie> results = null;

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
