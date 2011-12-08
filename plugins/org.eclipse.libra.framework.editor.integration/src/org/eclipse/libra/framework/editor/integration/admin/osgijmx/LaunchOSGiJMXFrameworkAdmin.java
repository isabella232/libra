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
package org.eclipse.libra.framework.editor.integration.admin.osgijmx;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.libra.framework.editor.integration.internal.IntegrationPlugin;

/**
 * @author Kaloyan Raev
 */
public class LaunchOSGiJMXFrameworkAdmin extends AbstractOSGiJMXFrameworkAdmin {
	
	private ILaunch launch;
	
	public LaunchOSGiJMXFrameworkAdmin(ILaunch launch) {
		this.launch = launch;
	}
	
	@Override
	protected String getHost() throws CoreException {
		return "localhost";
	}

	@Override
	protected String getPort() throws CoreException {
		return getJmxPort(launch);
	}

	public static String getJmxPort(ILaunch launch) throws CoreException {
		String rawVMArgs = IntegrationPlugin.getProcess(launch).getAttribute(IProcess.ATTR_CMDLINE);
		if (rawVMArgs == null) {
			throw IntegrationPlugin.newCoreException(Messages.OSGiJMXFrameworkAdmin_CannotGetCmdLineArgs);
		}

		String port = null;
		String[] vmArgs = DebugPlugin.parseArguments(rawVMArgs);
		for (String arg : vmArgs) {
			if (arg.startsWith("-Dcom.sun.management.jmxremote.port=")) { //$NON-NLS-1$
				int index = arg.indexOf('=');
				port = arg.substring(index + 1).trim();	
			}
		}
		
		if (port == null) {
			throw IntegrationPlugin.newCoreException(Messages.OSGiJMXFrameworkAdmin_JmxRemoteNotConfigured); 
		}
		
		return port;
	}

}
