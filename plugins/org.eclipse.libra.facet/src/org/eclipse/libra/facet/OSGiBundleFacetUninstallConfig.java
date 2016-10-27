/*******************************************************************************
 * Copyright (c) 2010, 2011 SAP AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kaloyan Raev (SAP AG) - initial API and implementation
 *******************************************************************************/
package org.eclipse.libra.facet;

import java.util.ArrayList;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.SelectObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.wst.common.project.facet.core.ActionConfig;

public class OSGiBundleFacetUninstallConfig extends ActionConfig {
	
	private SelectObservableValue<OSGiBundleFacetUninstallStrategy> strategyValue;
	private ArrayList<WritableValue<Boolean>> optionValues;
	
	public OSGiBundleFacetUninstallConfig() {
		Realm realm = OSGiBundleFacetRealm.getRealm();
		
		strategyValue = new SelectObservableValue<OSGiBundleFacetUninstallStrategy>(realm, OSGiBundleFacetUninstallStrategy.class);
		
		final OSGiBundleFacetUninstallStrategy[] values = OSGiBundleFacetUninstallStrategy.values();
		
		optionValues =  new ArrayList<WritableValue<Boolean>>(values==null ? 0 : values.length);
		if (values!=null) for (final OSGiBundleFacetUninstallStrategy val: values) {
			final WritableValue<Boolean> wv = new WritableValue<Boolean>(realm, null, Boolean.class);
			optionValues.add(wv);
			strategyValue.addOption(val, wv);
		}
		strategyValue.setValue(OSGiBundleFacetUninstallStrategy.defaultStrategy());
	}

	public SelectObservableValue<OSGiBundleFacetUninstallStrategy> getStrategyValue() {
		return strategyValue;
	}
	
	public OSGiBundleFacetUninstallStrategy getStrategy() {
		return strategyValue.getValue();
	}
	
	public void setStrategy(OSGiBundleFacetUninstallStrategy strategy) {
		strategyValue.setValue(strategy);
	}
	
	@SuppressWarnings("rawtypes")
	public WritableValue[] getOptionValues() {
		return optionValues.toArray(new WritableValue[0]);
	}
	
}
