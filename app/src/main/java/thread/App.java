/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package thread;

import java.io.IOException;
import java.math.BigInteger;

import thread.lecture.ConditionBasic;
import thread.lecture.DataConditionBasic;
import thread.lecture.InterruptBasic;
import thread.lecture.JoinBasic;
import thread.lecture.LockFreeBasic;
import thread.lecture.LockingStrategeBasic;
import thread.lecture.ObjectSignalBasic;
import thread.lecture.OptimizationBasic;
import thread.lecture.ReentrantReadWriteLockBasic;
import thread.lecture.SemaphoreBasic;
import thread.lecture.ThreadBasic;
import thread.lecture.ThroughputBasic;
import thread.lecture.VolatileBasic;
import thread.policeHacker.PoliceHacker;
import thread.quiz.Section2;

public class App {
    public static void main(String[] args) throws IOException{

        // Basic Thread Lecture
        // ThreadBasic threadBasic = new ThreadBasic();
        // threadBasic.execute();

        // PoliceHacker case study
        // PoliceHacker policeHacker = new PoliceHacker();
        // policeHacker.execute();

        // InterruptBasic interruptBasic = new InterruptBasic();
        // interruptBasic.execute();

        // JoinBasic joinBasic = new JoinBasic();
        // joinBasic.execute();

        // Section2 section2 = new Section2();
        // BigInteger result = section2.calculateResult(BigInteger.valueOf(21), BigInteger.valueOf(2), BigInteger.valueOf(3L), BigInteger.valueOf(2L));

        // OptimizationBasic optimizationBasic = new OptimizationBasic();
        // optimizationBasic.execute();

        // ThroughputBasic throughputBasic = new ThroughputBasic();
        // throughputBasic.execute();

        // ConditionBasic conditionBasic = new ConditionBasic();
        // conditionBasic.execute();

        // VolatileBasic volatileBasic = new VolatileBasic();
        // volatileBasic.execute();

        // DataConditionBasic dataConditionBasic = new DataConditionBasic();
        // dataConditionBasic.execute();

        // LockingStrategeBasic lockingStrategeBasic = new LockingStrategeBasic();
        // lockingStrategeBasic.execute();

        // ReentrantReadWriteLockBasic reentrantReadLockBasic = new ReentrantReadWriteLockBasic();
        // reentrantReadLockBasic.execute();

        // SemaphoreBasic semaphoreBasic = new SemaphoreBasic();
        // semaphoreBasic.execute();

        // ObjectSignalBasic objectSignalBasic = new ObjectSignalBasic();
        // objectSignalBasic.execute();

        LockFreeBasic lockFreeBasic = new LockFreeBasic();
        lockFreeBasic.execute();
    }
}
