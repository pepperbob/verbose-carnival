
package de.byoc.sequencr;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.mapdb.Atomic;
import org.mapdb.DB;

public class SequencerHandler implements Handler<RoutingContext> {

  private final Map<String, Atomic.Long> sequences = new ConcurrentHashMap<>();
  private final DB db;
  
  public SequencerHandler(DB db) {
      this.db = db;
  }

  @Override
  public void handle(RoutingContext e) {
    String sequence = e.request().getParam("seq");
    Atomic.Long atomic = sequences
            .computeIfAbsent(sequence, s -> db.atomicLong(s).createOrOpen());
    e.response().end(String.valueOf(atomic.incrementAndGet()));
  }

}
