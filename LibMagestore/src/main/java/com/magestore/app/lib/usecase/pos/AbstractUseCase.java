package com.magestore.app.lib.usecase.pos;

import com.magestore.app.lib.usecase.UseCase;
import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.context.MagestoreProgress;

/**
 * Created by Mike on 12/22/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class AbstractUseCase implements UseCase {
    private MagestoreProgress mProgress;
    private MagestoreContext mContext;

    @Override
    public void setContext(MagestoreContext context) {
        mContext = context;
    }

    @Override
    public void setProgress(MagestoreProgress progress) {
        mProgress = progress;
    }

    @Override
    public MagestoreContext getContext() {
        return mContext;
    }

    @Override
    public MagestoreProgress getProgress() {
        return mProgress;
    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }


}
