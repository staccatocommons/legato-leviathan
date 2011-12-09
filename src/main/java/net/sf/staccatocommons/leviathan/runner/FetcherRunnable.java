package net.sf.staccatocommons.leviathan.runner;

import java.util.List;

import net.sf.staccatocommons.control.monad.Monad;
import net.sf.staccatocommons.defs.Thunk;
import net.sf.staccatocommons.restrictions.check.NonNull;
import net.sf.staccatocommons.restrictions.check.NotEmpty;
import ar.com.zauber.leviathan.api.AsyncUriFetcher;

public class FetcherRunnable implements Runnable {

  private final AsyncUriFetcher fetcher;
  private final List<Thunk<Monad<Void>>> scrapperFactories;

  public FetcherRunnable(@NonNull AsyncUriFetcher fetcher,
    @NotEmpty List<Thunk<Monad<Void>>> scrapperFactories) {
    this.fetcher = fetcher;
    this.scrapperFactories = scrapperFactories;
  }

  @Override
  public void run() {
    for (Thunk<Monad<Void>> target : scrapperFactories) {
      target.value().value();
    }
    try {
      fetcher.awaitIdleness();
    } catch (InterruptedException e) {
      // ignore
    }
    fetcher.shutdownNow();
  }

}
