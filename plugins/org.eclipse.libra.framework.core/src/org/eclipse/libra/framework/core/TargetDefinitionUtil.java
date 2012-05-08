package org.eclipse.libra.framework.core;

import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.target.provisional.ITargetPlatformService;

@SuppressWarnings("restriction")
public class TargetDefinitionUtil {
	
	public static ITargetPlatformService getTargetPlatformService() {
		ITargetPlatformService service = (ITargetPlatformService) PDECore.getDefault().acquireService(ITargetPlatformService.class.getName());
		return service;
	}


}
