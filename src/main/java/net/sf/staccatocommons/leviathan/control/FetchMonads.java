package net.sf.staccatocommons.leviathan.control;

import ar.com.zauber.leviathan.api.AsyncUriFetcher;
import ar.com.zauber.leviathan.api.URIFetcher;
import ar.com.zauber.leviathan.api.URIFetcherResponse;
import net.sf.staccatocommons.control.monad.Monad;
import net.sf.staccatocommons.control.monad.Monads;
import net.sf.staccatocommons.leviathan.fetcher.FetchMethod;

public class FetchMonads {

  public static Monad<URIFetcherResponse> from(FetchMethod fetchMethod, AsyncUriFetcher asyncUriFetchers,
      URIFetcher uriFetcher) {
    return Monads.from(new FetchMonadicValue(fetchMethod, asyncUriFetchers, uriFetcher));
  }
}
