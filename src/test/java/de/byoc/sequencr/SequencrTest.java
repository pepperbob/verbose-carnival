package de.byoc.sequencr;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class SequencrTest {

  private Vertx vertx;

  @Before
  public void setup(TestContext context) {
    vertx = Vertx.vertx();
    vertx.deployVerticle(new Sequencer(), context.asyncAssertSuccess());
  }
  
  @After
  public void teatdown(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void test(TestContext context) {
    
    Async async = context.async();

    HttpClient now = vertx.createHttpClient();
            now.getNow(8080, "localhost", "/sequencer/abc",
                    r -> r.bodyHandler(b -> {
                      context.assertEquals(b.toString(), "4");
                      now.close();
                      async.complete();
                    }));
  }
}

