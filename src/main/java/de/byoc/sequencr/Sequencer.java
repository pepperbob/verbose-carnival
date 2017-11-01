
package de.byoc.sequencr;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.mapdb.DB;
import org.mapdb.DBMaker;

public class Sequencer extends AbstractVerticle {
  
  private final DB db;

  public Sequencer() {
    db = DBMaker.fileDB("sequencer").closeOnJvmShutdown().make();
  }
  
  @Override
  public void start(Future<Void> fut) throws Exception {
    Router router = Router.router(vertx);
    router.get("/sequencer/:seq").handler(new SequencerHandler(db));
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    fut.complete();
  }
  
  @Override
  public void stop(Future<Void> callback) throws Exception {
    db.close();
    callback.complete();
  }
  
  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    Runtime.getRuntime().addShutdownHook(new Thread(() -> vertx.close()));
    vertx.deployVerticle(new Sequencer());
  }
  
}
