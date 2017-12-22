package com.nctc2017.services.utils;


public class AutoDecisionTask implements Runnable {
    private int delay;
    private Visitor decisionVisitor;

    public AutoDecisionTask(Visitor decisionVisitor, int delay) {
            this.decisionVisitor = decisionVisitor;
            this.delay = delay;
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
