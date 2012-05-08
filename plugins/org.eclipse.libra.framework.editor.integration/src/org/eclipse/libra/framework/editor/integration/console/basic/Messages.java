/*******************************************************************************
 * Copyright (c) 2011 SAP AG
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 */
package org.eclipse.libra.framework.editor.integration.console.basic;

import org.eclipse.osgi.util.NLS;

/**
 * @author Kaloyan Raev
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.libra.framework.editor.integration.console.basic.messages"; //$NON-NLS-1$
	public static String BasicOSGiFrameworkConsole_CannotGetInOutStreams;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
