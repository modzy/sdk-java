package com.modzy.sdk.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerFactory {

	static {
		InputStream is = LoggerFactory.class.getResourceAsStream("/logging.properties");		
		try {
			LogManager.getLogManager().readConfiguration(is);
		}
		catch(IOException ioe) {
			Logger.getAnonymousLogger().log(Level.WARNING, "Custom logging configuration not found, using jvm defaults ", ioe);
		}
		finally {
			try {
				is.close();
			}
			catch( Throwable e ) {
				
			}
		}
	}
	
	public static final Logger getLogger(Object myClass) {		
		return Logger.getLogger(myClass.getClass().getSimpleName());
	}
	
}
