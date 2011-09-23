/*******************************************************************************
 *    Copyright (c) 2010 Eteration A.S. and others.
 *    All rights reserved. This program and the accompanying materials
 *    are made available under the terms of the Eclipse Public License v1.0
 *    which accompanies this distribution, and is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 *    
 *     Contributors:
 *        Eteration A.S. - initial API and implementation
 *        Naci Dai and Murat Yener, Eteration A.S. 
 *******************************************************************************/

package org.eclipse.libra.framework.ui.internal.actions;

import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IObjectActionDelegate;

public class SyncDebugAction extends AbstractSyncLaunch implements IObjectActionDelegate {

	public void run(IAction action) {
		launchMode = ILaunchManager.DEBUG_MODE;
		
		super.run(action);
	}

}
