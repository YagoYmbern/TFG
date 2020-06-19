/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import exception.ExceptionDemography;
import exception.ExceptionPopulation;
import exception.ParameterException;
import static java.awt.SystemColor.window;
import parametersSimulation.ParametersGenome;
import simulator.AdmixtureModel;




/**
 *
 * @author yagoy
 */
public class HelloRunnable{

    public static class MyRunnable implements Runnable{
        @Override
        public void run(){
            System.out.println("Hello");
        }
    }
    public class MyThread extends Thread{
        public void run(){
            System.out.println("MyThread running");
        }
    }
    public static void main(String[] args){
        Thread myThread = new Thread(new MyRunnable(), "MyThread");
        Thread myThread2 = new Thread(new MyRunnable(), "MyThread");
   }
}

    
