package net.sf.staccatocommons.leviathan.control;

import net.sf.staccatocommons.control.monad.Monad;
import net.sf.staccatocommons.control.monad.MonadicValue;
import net.sf.staccatocommons.control.monad.Monads;
import net.sf.staccatocommons.defs.Applicable;
import net.sf.staccatocommons.leviathan.fetcher.FetchMethod;
import ar.com.zauber.commons.dao.Closure;
import ar.com.zauber.leviathan.api.AsyncUriFetcher;
import ar.com.zauber.leviathan.api.URIFetcher;
import ar.com.zauber.leviathan.api.URIFetcherResponse;

public class FetchMonadicValue implements MonadicValue<URIFetcherResponse> {

   private final FetchMethod fetchMethod;
   private final AsyncUriFetcher asyncUriFetcher;
   private final URIFetcher uriFetcher;

   public FetchMonadicValue(FetchMethod fetchMethod, AsyncUriFetcher asyncUriFetcher, URIFetcher uriFetcher) {
      this.fetchMethod = fetchMethod;
      this.asyncUriFetcher = asyncUriFetcher;
      this.uriFetcher = uriFetcher;
   }
   public <T> void eval(final Applicable<? super URIFetcherResponse, Monad<T>> arg0) {
      fetchMethod.fetch(asyncUriFetcher, uriFetcher, new Closure<URIFetcherResponse>() {
         public void execute(URIFetcherResponse t) {
            arg0.apply(t).value();
         }
      });
   }

}
