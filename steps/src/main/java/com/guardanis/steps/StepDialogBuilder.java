package com.guardanis.steps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.view.View;

import com.guardanis.steps.StepController.StepEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class StepDialogBuilder implements StepController.StepEventListener {

    protected Dialog dialog;

    protected List<StepModule> modules;
    protected StepEventListener eventListener;

    protected boolean cancelable = false;

    public StepDialogBuilder(@NonNull List<StepModule> modules){
        this.modules = modules;
    }

    public StepDialogBuilder setEventListener(@Nullable StepEventListener eventListener){
        this.eventListener = eventListener;
        return this;
    }

    public StepDialogBuilder setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public StepController show(@NonNull Activity activity){
        View v = activity.getLayoutInflater()
                .inflate(R.layout.step__content, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setCancelable(cancelable)
                .setView(v);

        dialog = builder.show();

        return new StepController(v, modules, this);
    }

    @Override
    public void onStepLoaded(StepModule module, int index) {
        if(eventListener != null)
            eventListener.onStepLoaded(module, index);
    }

    @Override
    public void onFinished() {
        safelyDismissDialog();

        if(eventListener != null)
            eventListener.onFinished();
    }

    @Override
    public void onSkipped() {
        safelyDismissDialog();

        if(eventListener != null)
            eventListener.onSkipped();
    }

    public void safelyDismissDialog() {
        try{
            // Attempt to prevent stupid fucking uncatchable system error "View not Attached to Window" when dialog dismissed
            Context context = ((ContextWrapper) dialog.getContext()).getBaseContext();
            if(context instanceof Activity) {
                if(!((Activity) context).isFinishing()){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if(!((Activity) context).isDestroyed())
                            dialog.dismiss();
                    }
                    else dialog.dismiss();
                }
            }
            else dialog.dismiss();
        }
        catch(IllegalArgumentException e){ }
        catch(Throwable e){ }

        this.dialog = null;
    }
}
