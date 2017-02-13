package com.caplin.template.jtm7;

import com.caplin.jtm.TransformerAccessor;
import com.caplin.jtm.TransformerModule;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TemplateTransformerModule implements TransformerModule {

    private Logger logger = null;
    private String moduleName = getClass().getName();

    @Override
    public void initialise(String moduleName, TransformerAccessor transformerAccessor) {
        logger = transformerAccessor.getLogger();
        this.moduleName = moduleName;

        logger.log(Level.INFO, "Template jtm loading!");
    }


    @java.lang.Override
    public void setFileReading(boolean fileReading) {

    }

    @java.lang.Override
    public void shutdown() {
        if (logger != null) {
            logger.log(Level.INFO, "Shutting down module " + moduleName);
        }
    }
}
