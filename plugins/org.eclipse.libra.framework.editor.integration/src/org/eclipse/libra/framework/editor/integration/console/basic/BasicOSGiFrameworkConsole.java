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

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.libra.framework.editor.core.IOSGiFrameworkConsole;
import org.eclipse.libra.framework.editor.integration.internal.IntegrationPlugin;

/**
 * @author Kaloyan Raev
 */
public class BasicOSGiFrameworkConsole implements IOSGiFrameworkConsole, IStreamListener {
	
	private IStreamsProxy proxy;
	private StringBuilder result;

	public BasicOSGiFrameworkConsole(IStreamsProxy proxy) {
		this.proxy = proxy;
		proxy.getOutputStreamMonitor().addListener(this);
	}

	public synchronized String executeCommand(String command) throws CoreException {
		result = new StringBuilder();
		
		try {
			proxy.write(command + "\n");
		} catch (IOException e) {
			throw new CoreException(IntegrationPlugin.newErrorStatus(e));
		}
		
		long startTime = System.currentTimeMillis();
		int size = 0;
		do {
			size = result.length();
			try {
				wait(10);
			} catch (InterruptedException e) {
				throw new CoreException(IntegrationPlugin.newErrorStatus(e));
			}
		} while ((result.length() == 0 && System.currentTimeMillis() - startTime < 5000)
				|| result.length() > size);
		
		return result.toString();
	}

	public synchronized void streamAppended(String text, IStreamMonitor monitor) {
		result.append(text);
	}

}
