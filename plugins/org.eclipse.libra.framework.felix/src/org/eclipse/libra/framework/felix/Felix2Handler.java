/*******************************************************************************
 *   Copyright (c) 2010 Eteration A.S. and others.
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 *  
 *   Contributors:
 *      Naci Dai and Murat Yener, Eteration A.S. - Initial API and implementation
 *******************************************************************************/
package org.eclipse.libra.framework.felix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.libra.framework.core.Trace;
import org.eclipse.wst.server.core.IModule;


public class Felix2Handler implements IFelixVersionHandler {



	public IStatus verifyInstallPath(IPath location) {

		File f = location.append("conf").toFile();
		if(f == null || !f.exists())
			return new Status(IStatus.ERROR, FelixPlugin.PLUGIN_ID,
					0, Messages.warningCantReadConfig, null);
		File[] conf = f.listFiles();
		if (conf != null) {
			int size = conf.length;
			for (int i = 0; i < size; i++) {
				if (!f.canRead())
					return new Status(IStatus.WARNING, FelixPlugin.PLUGIN_ID,
							0, Messages.warningCantReadConfig, null);
			}
		}

		return Status.OK_STATUS;
	}

	public String getFrameworkClass() {
		return "org.apache.felix.main.Main";
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getFrameworkClasspath(IPath installPath, IPath configPath) {

		List cp = new ArrayList();

		IPath binPath = installPath.append("bin");
		if (binPath.toFile().exists()) {
			IPath path = binPath.append("felix.jar");
			cp.add(JavaRuntime.newArchiveRuntimeClasspathEntry(path));
		}

		return cp;
	}


	public String[] getFrameworkProgramArguments(IPath configPath, boolean debug,
			boolean starting) {
		return new String[]{configPath.append("cache").makeAbsolute().toPortableString()};
	}


	public String[] getExcludedFrameworkProgramArguments(boolean debug,
			boolean starting) {
		return null;
	}


	public String[] getFrameworkVMArguments(IPath installPath, IPath configPath,
			IPath deployPath, boolean isTestEnv, boolean jmxEnabled, int jmxPort) {
		
		String configPathStr = deployPath.makeAbsolute().append("config.properties").toPortableString(); //$NON-NLS-1$
		String vmArgs = "-Dfelix.config.properties=file:" + configPathStr; //$NON-NLS-1$
	
		//String configPathStr = deployPath.makeAbsolute().toOSString();
		String profilePath =  deployPath.append("java.profile").toOSString();
	

		String vmArgs2 =""  ;
		try {
			copyFile(this.getClass().getResourceAsStream("java6-server.profile"), new File(profilePath));
				vmArgs2 += "-Dosgi.java.profile=file:"+profilePath; //$NON-NLS-1$ //$NON-NLS-2$
		} catch (IOException e) {
			Trace.trace(Trace.SEVERE, "Could not set equinox VM arguments:"+e.getMessage(), e);
		}
	


		if(jmxEnabled)
			return new String[]{"-Dcom.sun.management.jmxremote.port="+jmxPort, "-Dcom.sun.management.jmxremote.authenticate=false", "-Dcom.sun.management.jmxremote.ssl=false", vmArgs, vmArgs2};
		else
			return new String[]{ vmArgs, vmArgs2};
	}

	private  void copyFile(InputStream source, File destFile) throws IOException {


		FileOutputStream destination = null;
		 try {
		  destination = new FileOutputStream(destFile);
		  int c;
		  while((c = source.read()) != -1){
			  destination.write(c);
		  }
		 }
		 finally {
		  if(source != null) {
		   source.close();
		  }
		  if(destination != null) {
		   destination.close();
		  }
		}
	}
	
	public IStatus canAddModule(IModule module) {
		String id =  module.getModuleType().getId();
		//String version = module.getModuleType().getVersion();
		if ("osgi.bundle".equals(id) )
			return Status.OK_STATUS;

		return new Status(IStatus.ERROR, FelixPlugin.PLUGIN_ID, 0,
				Messages.errorNotBundle, null);
	}


	public IStatus prepareFrameworkInstanceDirectory(IPath baseDir) {
		return Status.OK_STATUS;//TomcatVersionHelper.createCatalinaInstanceDirectory(baseDir);
	}


	public IStatus prepareDeployDirectory(IPath deployPath) {
		
		if (Trace.isTraceEnabled())
			Trace.trace(Trace.FINER, "Creating runtime directory at " + deployPath.toOSString());
		

		// Prepare a felix directory structure
		File temp = deployPath.append("plugins").toFile();
		if (!temp.exists())
			temp.mkdirs();
		temp = deployPath.append("auto").toFile();
		if (!temp.exists())
			temp.mkdirs();
		temp = deployPath.append("cache").toFile();
		if (!temp.exists())
			temp.mkdirs();

		return Status.OK_STATUS;		
	}



	public boolean supportsServeModulesWithoutPublish() {
		return true;
	}

	public void prepareFrameworkConfigurationFile(IPath configPath,	String workspaceBundles, String kernelBundles) {
			Properties properties = new Properties();

			properties.setProperty("felix.auto.deploy.dir",configPath.append("auto").makeAbsolute().toPortableString());
			properties.setProperty("felix.auto.deploy.action", "install,start");
			properties.setProperty("org.osgi.framework.startlevel.beginning", "2");
			properties.setProperty("felix.auto.install.1", kernelBundles);
			properties.setProperty("felix.auto.start.1", kernelBundles);
			properties.setProperty("felix.auto.install.2", workspaceBundles);
			properties.setProperty("felix.auto.start.2", workspaceBundles);
			properties.setProperty("org.osgi.framework.storage", "file:"
					+ configPath.append("auto").makeAbsolute().toPortableString());
			properties.setProperty("org.osgi.framework.storage.clean","onFirstInit");

			try {
				properties.store(new FileOutputStream(configPath.append("config.properties").makeAbsolute().toFile()), "## AUTO GENERATED ##");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
