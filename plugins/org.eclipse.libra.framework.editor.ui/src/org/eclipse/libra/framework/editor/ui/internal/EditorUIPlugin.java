/*******************************************************************************
 * Copyright (c) 2011 SAP AG
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.libra.framework.editor.ui.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Kaloyan Raev
 */
public class EditorUIPlugin extends AbstractUIPlugin {
	
	public static final String PLUGIN_ID = "org.eclipse.libra.framework.editor";

	public EditorUIPlugin() {
		super();
	}

	/**
	 * Constructs new IStatus object from the given String message. 
	 * 
	 * @param message the message to create the IStatus object from. 
	 */
	public static IStatus newErrorStatus(String message) {
		return new Status(IStatus.ERROR, PLUGIN_ID, message);
	}

	/**
	 * Constructs new IStatus object from the given Throwable object. 
	 * 
	 * @param t the Throwable object to create the IStatus message from. 
	 */
	public static IStatus newErrorStatus(Throwable t) {
		return new Status(IStatus.ERROR, PLUGIN_ID, t.getMessage(), t);
	}
	
	/**
	 * Logs the specified status with this plug-in's log.
	 * 
	 * @param status status to log
	 */
	public static void log(IStatus status) {
		Bundle bdl = FrameworkUtil.getBundle(EditorUIPlugin.class);
		ILog log = bdl==null ? null : Platform.getLog(bdl);
		if (log!=null) log.log(status);
	}
	
	/**
	 * Logs the specified throwable with this plug-in's log.
	 * 
	 * @param t throwable to log
	 */
	public static void log(Throwable t) {
		if (t instanceof CoreException) {
			log(((CoreException) t).getStatus());
		} else {
			log(newErrorStatus(t));
		}
	}

}
