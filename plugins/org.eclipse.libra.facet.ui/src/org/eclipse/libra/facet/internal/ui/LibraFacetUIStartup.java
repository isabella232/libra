/*******************************************************************************
 * Copyright (c) 2012 SAP AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kaloyan Raev (SAP AG) - initial API and implementation
 *******************************************************************************/
package org.eclipse.libra.facet.internal.ui;

import org.eclipse.libra.facet.OSGiBundleFacetUtils;
import org.eclipse.ui.IStartup;

public class LibraFacetUIStartup implements IStartup {

	public void earlyStartup() {
		// Dummy call to ensure that the org.eclipse.libra.facet plug-in is loaded
		// We need this to have the WebContextRootSynchronizer registered as resource change listener
		OSGiBundleFacetUtils.class.getName();
	}

}
