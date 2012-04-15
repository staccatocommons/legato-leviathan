package net.sf.staccatocommons.leviathan.fetcher;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.springframework.core.io.Resource;

import ar.com.zauber.commons.dao.Closure;
import ar.com.zauber.leviathan.api.AsyncUriFetcher;
import ar.com.zauber.leviathan.api.URIFetcher;
import ar.com.zauber.leviathan.api.URIFetcherResponse;
import ar.com.zauber.leviathan.common.InmutableURIAndCtx;

import net.sf.staccatocommons.lang.SoftException;

public class FetchMethods {

   public static FetchMethod fromGet(final URI uri, final Map<String, Object> context) {
      return new FetchMethod() {
         @Override
         public void fetch(AsyncUriFetcher fetcher, URIFetcher uriFetcher, Closure<URIFetcherResponse> closure) {
            fetcher.scheduleFetch(uriFetcher.createGet(new InmutableURIAndCtx(uri, context)), closure);
         }
      };
   }

   public static FetchMethod fromGet(final URI uri) {
      return fromGet(uri, Collections.<String, Object> emptyMap());
   }

   public static FetchMethod fromPost(final URI uri, final Resource resource) {
      return new FetchMethod() {
         @Override
         public void fetch(AsyncUriFetcher fetcher, URIFetcher uriFetcher, Closure<URIFetcherResponse> closure) {
            try {
               fetcher.scheduleFetch(
                     uriFetcher.createPost(new InmutableURIAndCtx(uri), resource.getInputStream()), closure);
            } catch (IOException e) {
               throw SoftException.soften(e);
            }
         }
      };
   }

   public static FetchMethod fromGet(String string) {
      return fromGet(URI.create(string));
   }

}
