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
package org.eclipse.libra.framework.editor.integration.console.basic;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.libra.framework.editor.integration.internal.IntegrationPlugin;

/**
 * @author Kaloyan Raev
 */
public class LaunchBasicOSGiFrameworkConsole extends BasicOSGiFrameworkConsole {
	
	private ILaunch launch;
	
	public LaunchBasicOSGiFrameworkConsole(ILaunch launch) {
		super();
		this.launch = launch;
	}

	@Override
	protected IStreamsProxy getProxy() throws CoreException {
		return getStreamsProxy(launch);
	}

	public static IStreamsProxy getStreamsProxy(ILaunch launch) throws CoreException {
		IStreamsProxy proxy = IntegrationPlugin.getProcess(launch).getStreamsProxy();
		if (proxy == null) {
			throw IntegrationPlugin.newCoreException(Messages.BasicOSGiFrameworkConsole_CannotGetInOutStreams);
		}
		return proxy;
	}

}
