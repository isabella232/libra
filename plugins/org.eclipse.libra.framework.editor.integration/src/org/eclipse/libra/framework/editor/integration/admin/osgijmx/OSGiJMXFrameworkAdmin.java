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

/**
 * @author Kaloyan Raev
 */
public class OSGiJMXFrameworkAdmin extends AbstractOSGiJMXFrameworkAdmin {
	
	private String host;
	private String port;
	
	public OSGiJMXFrameworkAdmin(String host, String port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	protected String getHost() throws CoreException {
		return host;
	}
	
	@Override
	protected String getPort() throws CoreException {
		return port;
	}

}
