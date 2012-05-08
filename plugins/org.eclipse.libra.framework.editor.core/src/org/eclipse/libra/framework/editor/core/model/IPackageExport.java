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
package org.eclipse.libra.framework.editor.core.model;

/**
 * This interface represents a package exported by an OSGi bundle represented by
 * an {@code IBundle} object.
 * 
 * @see IBundle
 * 
 * @author Kaloyan Raev
 */
public interface IPackageExport {

	/**
	 * Returns the name of the exported package.
	 * 
	 * @return The fully qualified name of the package.
	 */
	public String getName();

	/**
	 * Returns the version of the exported package.
	 * 
	 * @return The String representation of the version.
	 */
	public String getVersion();

}
