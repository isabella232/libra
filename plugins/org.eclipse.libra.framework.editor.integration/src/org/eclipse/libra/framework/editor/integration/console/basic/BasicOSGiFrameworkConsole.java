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
import org.eclipse.debug.core.model.IStreamsProxy;

/**
 * @author Kaloyan Raev
 */
public class BasicOSGiFrameworkConsole extends AbstractBasicOSGiFrameworkConsole {
	
	private IStreamsProxy proxy;
	
	protected BasicOSGiFrameworkConsole() {
		this.proxy = null;
	}

	public BasicOSGiFrameworkConsole(IStreamsProxy proxy) {
		this.proxy = proxy;
	}
	
	@Override
	protected IStreamsProxy getProxy() throws CoreException {
		return proxy;
	}

}
