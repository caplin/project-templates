package com.caplin.template.jtm;

import com.caplin.server.logging.ServerLevel;
import com.caplin.transformer.module.TransformerAccessor;
import com.caplin.transformer.module.TransformerModule;
import com.caplin.transformer.module.pipeline.PipelineRegistrarImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class TemplateTransformerModule implements TransformerModule {

    private Logger logger = null;
    private String moduleName = getClass().getName();

    @java.lang.Override
    public void initialise(java.lang.String moduleName, TransformerAccessor transformerAccessor) {
        logger = transformerAccessor.getLogger();
        this.moduleName = moduleName;

        logger.log(ServerLevel.INFO_LEVEL, "Template module loading!");

    }

    @java.lang.Override
    public void setFileReading(boolean fileReading) {

    }

    @java.lang.Override
    public void shutdown() {
        if (logger != null) {
            logger.log(ServerLevel.INFO_LEVEL, "Shutting down module " + moduleName);
        }
    }

    @java.lang.Override
    public java.lang.String getLoggerName() {
        return "NotificationsExtension";
    }
}
