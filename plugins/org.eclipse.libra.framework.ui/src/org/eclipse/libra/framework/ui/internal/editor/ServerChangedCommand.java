/*******************************************************************************
 *    Copyright (c) 2012 Eteration A.S. and others.
 *    All rights reserved. This program and the accompanying materials
 *    are made available under the terms of the Eclipse Public License v1.0
 *    which accompanies this distribution, and is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 *    
 *     Contributors:
 *       Naci Dai and Murat Yener, Eteration A.S. 
 *******************************************************************************/
package org.eclipse.libra.framework.ui.internal.editor;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.libra.framework.core.IOSGIFrameworkInstance;




/**
 * This command does nothing but execute sets the dirty for the editor 
 * because the content of the target definition has changed.
 */
public class ServerChangedCommand extends AbstractOperation {

	public ServerChangedCommand(IOSGIFrameworkInstance  instance) {
		super( "SERVER CHANGED...");
	
	}



	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return Status.OK_STATUS;
	}
}
