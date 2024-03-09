package com.aki.mcutils.APICore.Utils;

import com.aki.mcutils.APICore.Handlers.ProcessHandler;

public abstract class DelayedExcutor implements IProcessMethod {
    private int delay;
    private Object[] args;
    private boolean hasExecuted = false;

    public DelayedExcutor(int delay, Object... args) {
        this.delay = delay;
        this.args = args;
    }

    public abstract void execute(Object[] args);

    public void run() {
        ProcessHandler.addProcessEvent(this);
    }

    @Override
    public void ClientUpdateProcess() {

    }

    @Override
    public void ServerUpdateProcess() {
        if (delay <= 0 && !hasExecuted) {
            execute(args);
            hasExecuted = true;
        }

        delay--;
    }

    @Override
    public boolean IsDeadProcess() {
        return hasExecuted;
    }
}
