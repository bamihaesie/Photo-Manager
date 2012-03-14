package com.bogdan.photomanager.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JProgressBar;

public class ProgressListener implements PropertyChangeListener {

    private JProgressBar progressBar;

    private ProgressListener() {}
    
    ProgressListener(JProgressBar progressBar) {
        this.progressBar = progressBar;
        this.progressBar.setValue(0);
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        String strPropertyName = evt.getPropertyName();
        if ("progress".equals(strPropertyName)) {
            progressBar.setIndeterminate(false);
            int progress = (Integer)evt.getNewValue();
            progressBar.setValue(progress);
        }
    }

}
