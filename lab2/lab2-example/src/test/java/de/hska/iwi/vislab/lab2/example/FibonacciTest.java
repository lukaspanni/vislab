package de.hska.iwi.vislab.lab2.example;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

public class FibonacciTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and
        // Main.startServer())
        // --
        // c.configuration().enable(new
        // org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void testGetInitialFibonacciNumber() {
        int response = target.path("fibonacci").request().accept(MediaType.TEXT_PLAIN).get(Integer.class);
        assertEquals(1, response);
    }

    @Test
    public void testIncrementFibonacciNumber() {
        target.path("fibonacci").request().accept(MediaType.WILDCARD).post(null);
        int response = target.path("fibonacci").request().accept(MediaType.TEXT_PLAIN).get(Integer.class);
        assertEquals(2, response);
        target.path("fibonacci").request().accept(MediaType.WILDCARD).post(null);
        int response2 = target.path("fibonacci").request().accept(MediaType.TEXT_PLAIN).get(Integer.class);
        assertEquals(3, response2);
    }

    @Test
    public void testResetFibonacciNumber() {
        target.path("fibonacci").request().accept(MediaType.WILDCARD).post(null);
        target.path("fibonacci").request().accept(MediaType.WILDCARD).post(null);
        target.path("fibonacci").request().accept(MediaType.WILDCARD).delete();
        int response = target.path("fibonacci").request().accept(MediaType.TEXT_PLAIN).get(Integer.class);
        assertEquals(1, response);
    }
}
