/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grpc.service;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.GreetWithDeadlineRequest;
import com.proto.calculator.GreetWithDeadlineResponse;
import com.proto.calculator.SquareRootRequest;
import com.proto.calculator.SquareRootResponse;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

/**
 *
 * @author Eder_Crespo
 */
public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void greetWithDeadline(GreetWithDeadlineRequest request, StreamObserver<GreetWithDeadlineResponse> responseObserver) {

        final Context current = Context.current();
        
        try {
            
            for(int i = 0; i < 3; i++) {
                
                if(!current.isCancelled()) {
                    System.out.println("Sleep for 100 ms");
                    Thread.sleep(100);
                } else {
                    return;
                }
                
            }

            System.out.println("Send response");
            responseObserver.onNext(GreetWithDeadlineResponse.newBuilder()
                    .setNameChanged("Hello ".concat(request.getName()))
                    .build());

            responseObserver.onCompleted();
            
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {
        
        Integer number = request.getNumber();
        
        if(number >= 0) {
            responseObserver.onNext(SquareRootResponse.newBuilder()
                    .setRootedNumber(Math.sqrt(number))
                    .build());
            
            responseObserver.onCompleted();
            
        } else {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .augmentDescription("More info about the error.")
                    .withDescription("The number being sent is not positive.")
                    .asRuntimeException());
        }
    }
}
