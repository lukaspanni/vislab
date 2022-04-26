package de.hska.iwi.vislab.lab2.example;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("fibonacci")
public class FibonacciService {

    //need to be static as a new instance is created for every request
    private static int currentFibonacciNumber = 1;
    private static int previousFibonacciNumber = 1;

    public FibonacciService() {
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public int getCurrentFibonacciNumber(){
        return currentFibonacciNumber;
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public void computeNextFibonacciNumber() {
        int result = currentFibonacciNumber + previousFibonacciNumber;
        previousFibonacciNumber = currentFibonacciNumber;
        currentFibonacciNumber = result;
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public void reset(){
        currentFibonacciNumber = 1;
        previousFibonacciNumber = 1;

    }
}
