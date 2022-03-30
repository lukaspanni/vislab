package de.hska.iwi.vislab.lab1.example.ws;

import javax.jws.WebService;

@WebService(endpointInterface = "de.hska.iwi.vislab.lab1.example.ws.FibonacciService")
public class FibonacciServiceImpl implements FibonacciService {

    public FibonacciServiceImpl(){}

    @Override
    public int getFibonacci(int n){
        if(n < 2) return 1;

        int[] fibonacciNumbers = new int[n];
        fibonacciNumbers[0] = 1;
        fibonacciNumbers[1] = 1;
        for (int i = 2; i < n; i++)
        {
            fibonacciNumbers[i] = fibonacciNumbers[i - 1] + fibonacciNumbers[i - 2];
        }
        return fibonacciNumbers[n-1];
    }
}
