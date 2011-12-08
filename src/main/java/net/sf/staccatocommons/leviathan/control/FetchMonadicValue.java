package net.sf.staccatocommons.leviathan.control;

import net.sf.staccatocommons.control.monad.Monad;
import net.sf.staccatocommons.control.monad.MonadicValue;
import net.sf.staccatocommons.defs.Applicable;
import net.sf.staccatocommons.leviathan.fetcher.FetchMethod;
import ar.com.zauber.commons.dao.Closure;
import ar.com.zauber.leviathan.api.AsyncUriFetcher;
import ar.com.zauber.leviathan.api.URIFetcherResponse;

public class FetchMonadicValue implements MonadicValue<URIFetcherResponse> {

  private final FetchMethod fetchMethod;
  private final AsyncUriFetcher asyncUriFetcher;

  public FetchMonadicValue(FetchMethod fetchMethod, AsyncUriFetcher asyncUriFetcher) {
    this.fetchMethod = fetchMethod;
    this.asyncUriFetcher = asyncUriFetcher;
  }

  public <T> void eval(final Applicable<? super URIFetcherResponse, Monad<T>> arg0) {
    fetchMethod.fetch(asyncUriFetcher, new Closure<URIFetcherResponse>() {
      public void execute(URIFetcherResponse t) {
        arg0.apply(t).value();
      }
    });
  }

}
