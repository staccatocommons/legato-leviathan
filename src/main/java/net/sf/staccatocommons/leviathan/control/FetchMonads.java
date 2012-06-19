package net.sf.staccatocommons.leviathan.control;

import net.sf.staccatocommons.control.monad.AbstractMonadicFunction;
import net.sf.staccatocommons.control.monad.Monad;
import net.sf.staccatocommons.control.monad.MonadicFunction;
import net.sf.staccatocommons.control.monad.Monads;
import net.sf.staccatocommons.leviathan.fetcher.FetchMethod;
import ar.com.zauber.leviathan.api.AsyncUriFetcher;
import ar.com.zauber.leviathan.api.URIFetcher;
import ar.com.zauber.leviathan.api.URIFetcherResponse;

public class FetchMonads {

  public static Monad<URIFetcherResponse> from(FetchMethod fetchMethod, AsyncUriFetcher asyncUriFetchers,
      URIFetcher uriFetcher) {
    return Monads.from(new FetchMonadicValue(fetchMethod, asyncUriFetchers, uriFetcher));
  }

  public static MonadicFunction<FetchMethod, URIFetcherResponse> fetch(final AsyncUriFetcher asyncUriFetcher,
      final URIFetcher uriFetcher) {
    return new AbstractMonadicFunction<FetchMethod, URIFetcherResponse>() {
      public Monad<URIFetcherResponse> apply(FetchMethod fetchMethod) {
        return from(fetchMethod, asyncUriFetcher, uriFetcher);
      }
    };
  }
}
