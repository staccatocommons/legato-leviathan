package net.sf.staccatocommons.leviathan.flow;

import java.net.URI;

import net.sf.staccatocommons.control.monad.Monad;
import net.sf.staccatocommons.control.monad.Monads;
import net.sf.staccatocommons.defs.Executable;
import net.sf.staccatocommons.defs.Thunk;
import net.sf.staccatocommons.defs.function.Function;
import net.sf.staccatocommons.leviathan.control.Fetch;
import net.sf.staccatocommons.leviathan.control.FetchMonadicValue;
import net.sf.staccatocommons.leviathan.fetcher.FetchMethod;
import net.sf.staccatocommons.leviathan.fetcher.FetchMethods;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.springframework.oxm.Unmarshaller;
import org.w3c.dom.Document;

import ar.com.zauber.leviathan.api.AsyncUriFetcher;
import ar.com.zauber.leviathan.api.URIFetcher;
import ar.com.zauber.leviathan.api.URIFetcherResponse;

public abstract class AbstractScrappingFlow implements Thunk<Monad<Void>> {

  private final AsyncUriFetcher fetcher;
  private final URIFetcher uriFetcher;
  private final Unmarshaller unmarshaller;


  public AbstractScrappingFlow(AsyncUriFetcher fetcher, URIFetcher uriFetcher, Unmarshaller unmarshaller) {
   this.fetcher = fetcher;
   this.uriFetcher = uriFetcher;
   this.unmarshaller = unmarshaller;
}

protected abstract URI baseUri();

  protected abstract Monad<?> entryPoint();

  protected final Monad<URIFetcherResponse> startOnBaseUri() {
    return startOn(baseUri());
  }

  protected final Monad<URIFetcherResponse> startOn(URI uri) {
    return fetch(FetchMethods.fromGet(uri));
  }

  protected final Monad<URIFetcherResponse> fetch(FetchMethod method) {
    return Monads.from(new FetchMonadicValue(method, fetcher, uriFetcher));
  }

  protected final URI resolve(String relative) {
    return baseUri().resolve(relative);
  }

  public final <A> Function<Document, A> unmarshall(Class<A> clazz) {
    return Fetch.parse(unmarshaller, clazz);
  }

  protected final Executable<HttpUriRequest> enableRedirect() {
    return new Executable<HttpUriRequest>() {
      public void exec(HttpUriRequest arg0) {
        HttpClientParams.setRedirecting(arg0.getParams(), true);
      }
    };
  }

  @Override
  public final Monad<Void> value() {
    return entryPoint().discard();
  }

}
