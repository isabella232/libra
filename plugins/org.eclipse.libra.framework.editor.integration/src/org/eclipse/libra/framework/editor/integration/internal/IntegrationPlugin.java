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
package org.eclipse.libra.framework.editor.integration.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;

/**
 * @author Kaloyan Raev
 */
public class IntegrationPlugin extends Plugin {
	
	public static final String PLUGIN_ID = "org.eclipse.libra.framework.editor.integration"; //$NON-NLS-1$
	
	public static IStatus newErrorStatus(Throwable t) {
		return newErrorStatus(t.getMessage(), t);
	}
	
	public static IStatus newErrorStatus(String message, Throwable t) {
		return new Status(IStatus.ERROR, PLUGIN_ID, message, t);
	}
	
	public static CoreException newCoreException(String errorMessage) {
		return new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, errorMessage));
	}
	
	public static IProcess getProcess(ILaunch launch) throws CoreException {
		IProcess[] processes = launch.getProcesses();
		if (processes.length == 0) {
			throw newCoreException(Messages.IntegrationPlugin_FrameworkNotStarted);
		}
		return processes[0];
	}

}
