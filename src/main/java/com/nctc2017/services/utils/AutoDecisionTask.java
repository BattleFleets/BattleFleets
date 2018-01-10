package com.nctc2017.services.utils;

import java.util.GregorianCalendar;

public class AutoDecisionTask implements Runnable {
    private int delay;
    private Visitor decisionVisitor;
    private long endTimePoint;

    public AutoDecisionTask(Visitor decisionVisitor, int delay) {
            this.decisionVisitor = decisionVisitor;
            this.delay = delay;
            this.endTimePoint = new GregorianCalendar().getTimeInMillis() + delay;
    }
    
    public long getTimeLeft() {
        return endTimePoint - new GregorianCalendar().getTimeInMillis();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            return;
        }
        decisionVisitor.visit();
    }

}
