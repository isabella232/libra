/*******************************************************************************
 *    Copyright (c) 2010 Eteration A.S. and others.
 *    All rights reserved. This program and the accompanying materials
 *    are made available under the terms of the Eclipse Public License v1.0
 *    which accompanies this distribution, and is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 *    
 *     Contributors:
 *        IBM Corporation - initial API and implementation
 *           - This code is based on WTP SDK frameworks and Tomcat Server Adapters
 *           org.eclipse.jst.server.core
 *           org.eclipse.jst.server.ui
 *           
 *       Naci Dai and Murat Yener, Eteration A.S. 
 *******************************************************************************/
package org.eclipse.libra.framework.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.osgi.service.debug.DebugOptions;
import org.eclipse.pde.core.project.IBundleProjectDescription;
import org.eclipse.pde.core.project.IBundleProjectService;
import org.eclipse.pde.core.target.ITargetPlatformService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;


public class FrameworkCorePlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.libra.framework.core"; //$NON-NLS-1$

	private ServiceReference<IBundleProjectService> bundleProjectServiceRef;
	private IBundleProjectService bundleProjectService;

	private ServiceReference<ITargetPlatformService> targetPlatformServiceRef;
	private ITargetPlatformService targetPlatformService;

	private static FrameworkCorePlugin plugin;

	/**
	 * The constructor
	 */
	public FrameworkCorePlugin() {
		super();
	}

	/**
	 * Tracing enabled?
	 * @return whether debug is enabled on the Platform (-debug) AND the {@value #PLUGIN_ID}/debug option
	 * is set to "true". 
	 */
	public static boolean isTraceEnabled() {
		/* This implementation is a mix of
		 *  https://wiki.eclipse.org/FAQ_How_do_I_use_the_platform_debug_tracing_facility
		 * and the previous (defacto deprecated) implementation in org.eclipse.core.runtime.Plugin 
		 */
		if (! Platform.inDebugMode()) return false;
		
		Bundle bdl = FrameworkUtil.getBundle(FrameworkCorePlugin.class);
		if (bdl == null){
			throw new RuntimeException("Could not resolve my own OSGi bundle, something seriously wrong with the Framework!"); //$NON-NLS-1$
		}
		String key = bdl.getSymbolicName() + "/debug"; //$NON-NLS-1$

		ServiceReference<DebugOptions> ref = bdl.getBundleContext().getServiceReference(DebugOptions.class);
		DebugOptions service = ref == null ? null : bdl.getBundleContext().getService(ref);

		// if platform debugging is enabled, check to see if this plugin is enabled for debugging
		if (service != null){
			if (! service.isDebugEnabled()) return false;
			return service.getBooleanOption(key, false);
		}

		return "true".equalsIgnoreCase(Platform.getDebugOption(key)); 	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		this.bundleProjectServiceRef = context.getServiceReference(IBundleProjectService.class);
		this.bundleProjectService = context.getService(bundleProjectServiceRef);

		this.targetPlatformServiceRef = context.getServiceReference(ITargetPlatformService.class);
		this.targetPlatformService = context.getService(targetPlatformServiceRef);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		context.ungetService(this.bundleProjectServiceRef);
		this.bundleProjectService=null;
		this.bundleProjectServiceRef=null;

		context.ungetService(this.targetPlatformServiceRef);
		this.targetPlatformService=null;
		this.targetPlatformServiceRef=null;

		plugin = null;
		super.stop(context);
	}

	public static ITargetPlatformService getTargetPlatformService() {
		if (plugin==null)
			return null;
		
		return plugin.targetPlatformService;
	}

	public static String getPreference(String id) {
		return Platform.getPreferencesService().getString(PLUGIN_ID, id, "", null);
	}
	
	public static void setPreference(String id, String value) {
		(new DefaultScope()).getNode(PLUGIN_ID).put(id, value);
	}
	
	public static IBundleProjectService getBundleProjectService() {
		if (plugin == null)
			return null;
		return plugin.bundleProjectService;
	}
	
	public static IBundleProjectDescription getDescription(IProject project) throws CoreException {
		try {
			return getBundleProjectService().getDescription(project);
		}
		catch (IllegalArgumentException e) {
			return null;
		}
	}
	
}
