/*******************************************************************************
 * Copyright (c) 2009, 2011 SpringSource, a divison of VMware, Inc. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SpringSource, a division of VMware, Inc. - initial API and implementation
 *     SAP AG - moving to Eclipse Libra project and enhancements
 *******************************************************************************/
package org.eclipse.libra.framework.editor.integration.internal.admin.osgijmx;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.libra.framework.editor.core.model.IServiceReference;

/**
 * @author Christian Dupuis
 * @author Kaloyan Raev
 */
public class ServiceReference implements IServiceReference {

	private final Long bundleId;

	private final String[] clazzes;

	private final Map<String, String> properties = new HashMap<String, String>();

	private final Set<Long> usingBundles = new HashSet<Long>();
	
	private final Type type;

	public ServiceReference(Type type, Long bundleId, String[] clazzes) {
		this.bundleId = bundleId;
		this.clazzes = clazzes;
		this.type = type;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public String[] getClazzes() {
		return clazzes;
	}
	
	public Set<Long> getUsingBundleIds() {
		return usingBundles;
	}

	public void addProperty(String key, String value) {
		this.properties.put(key, value);
	}

	public void addUsingBundle(Long id) {
		this.usingBundles.add(id);
	}

	public Long getBundleId() {
		return bundleId;
	}

	public Type getType() {
		return type;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode = 31 * hashCode + clazzes.hashCode();
		hashCode = 31 * hashCode + type.hashCode();
		return hashCode;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ServiceReference)) {
			return false;
		}
		ServiceReference that = (ServiceReference) other;
		if (!Arrays.equals(this.clazzes, that.clazzes)) {
			return false;
		}
		if (this.type != that.type) {
			return false;
		}
		if (this.type != null && !this.type.equals(that.type)) {
			return false;
		}
		return true;
	}
}
