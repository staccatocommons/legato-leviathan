package net.sf.staccatocommons.leviathan.fetcher;

import ar.com.zauber.commons.dao.Closure;
import ar.com.zauber.leviathan.api.AsyncUriFetcher;
import ar.com.zauber.leviathan.api.URIFetcher;
import ar.com.zauber.leviathan.api.URIFetcherResponse;

public interface FetchMethod {

  void fetch(AsyncUriFetcher fetcher, URIFetcher uriFetcher, Closure<URIFetcherResponse> closure);

}
