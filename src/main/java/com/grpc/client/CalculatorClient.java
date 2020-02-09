/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grpc.client;

//import grpc.DummyServiceGrpc;
import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.GreetWithDeadlineRequest;
import com.proto.calculator.GreetWithDeadlineResponse;
import com.proto.calculator.SquareRootRequest;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Eder_Crespo
 */
public class CalculatorClient extends ClientChannelService {

    public static void main(String[] args) {
        new CalculatorClient();
    }
    
    
    private void performDeadlineOperation(ManagedChannel channel) {
        
        final CalculatorServiceGrpc.CalculatorServiceBlockingStub blockingStub = CalculatorServiceGrpc.newBlockingStub(channel);

        
        try {
            
            System.out.println("Sending a request witha deadline of 100 ms");
            // first call 100 ms deadline
            final GreetWithDeadlineResponse response = blockingStub
                    .withDeadline(Deadline.after(100, TimeUnit.MILLISECONDS))
                    .greetWithDeadline(GreetWithDeadlineRequest.newBuilder()
                    .setName("George")
                    .build());
            
            System.out.println(response.getNameChanged());
            
        } catch(StatusRuntimeException e) {
            
            if(e.getStatus() == Status.DEADLINE_EXCEEDED) {
                System.out.println("Deadline has been exceeded!");
            } else {
                e.printStackTrace();
            }
        }  
    }
    
    private void performSquareRootOperation(ManagedChannel channel) {
        
        final CalculatorServiceGrpc.CalculatorServiceBlockingStub blockingStub = CalculatorServiceGrpc.newBlockingStub(channel);
    
        int number = -1;
        
        try {
            
            blockingStub.squareRoot(SquareRootRequest.newBuilder().setNumber(number).build());
            
        } catch(StatusRuntimeException e) {
            System.out.println("Got an exception for square root");
            e.printStackTrace();
        }  
    }

    @Override
    protected void performRequest(ManagedChannel channel) {
        performDeadlineOperation(channel);
    }

}
