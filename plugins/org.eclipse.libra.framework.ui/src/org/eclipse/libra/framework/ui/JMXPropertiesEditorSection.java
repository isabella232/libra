/*******************************************************************************
 *    Copyright (c) 2012 Eteration A.S. and others.
 *    All rights reserved. This program and the accompanying materials
 *    are made available under the terms of the Eclipse Public License v1.0
 *    which accompanies this distribution, and is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 *    
 *     Contributors:
 *       Naci Dai, Murat Yener 
 *******************************************************************************/

package org.eclipse.libra.framework.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.libra.framework.core.IOSGIFrameworkInstance;
import org.eclipse.libra.framework.ui.internal.SWTUtil;
import org.eclipse.libra.framework.ui.internal.editor.ServerChangedCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.wst.server.ui.editor.ServerEditorSection;

public class JMXPropertiesEditorSection extends ServerEditorSection {
	protected Section section;
	protected IOSGIFrameworkInstance frameworkInstance;

	protected PropertyChangeListener listener;
	Button jmxEnabled;
	Text jmxPortText;

	protected boolean updating = false;

	public JMXPropertiesEditorSection() {
		super();
	}

	/**
	 * Add listeners to detect undo changes and publishing of the server.
	 */
	protected void addChangeListeners() {
		listener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (updating)
					return;
				updating = true;
				if (IOSGIFrameworkInstance.PROPERTY_JMX_PORT.equals(event
						.getPropertyName())
						|| IOSGIFrameworkInstance.PROPERTY_JMX_ENABLED
								.equals(event.getPropertyName())) {
					validate();
				}
				updating = false;
			}
		};
		server.addPropertyChangeListener(listener);

	}

	/**
	 * Creates the SWT controls for this workbench part.
	 * 
	 * @param parent
	 *            the parent control
	 */
	public void createSection(Composite parent) {
		super.createSection(parent);
		FormToolkit toolkit = getFormToolkit(parent.getDisplay());
		section = toolkit.createSection(parent, ExpandableComposite.TWISTIE
				| ExpandableComposite.EXPANDED | ExpandableComposite.TITLE_BAR
				| Section.DESCRIPTION | ExpandableComposite.FOCUS_TITLE);
		section.setText(Messages.jmxSection);
		section.setDescription(Messages.jmxSectionDescription);
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL));

		Composite composite = toolkit.createComposite(section);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.verticalSpacing = 5;
		layout.horizontalSpacing = 15;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL));
		IWorkbenchHelpSystem whs = PlatformUI.getWorkbench().getHelpSystem();
		whs.setHelp(composite, ContextIds.FRAMEWORK_INSTANCE_EDITOR);
		whs.setHelp(section, ContextIds.FRAMEWORK_INSTANCE_EDITOR);
		toolkit.paintBordersFor(composite);
		section.setClient(composite);

		// JMX Selection	
		jmxEnabled = SWTUtil.createCheckButton(composite, Messages.jmxEnabled, 1);
		jmxEnabled.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent event) {
				handleEdit();
			}
			
			public void widgetDefaultSelected(SelectionEvent event) {
				handleEdit();
			}
		});
		
		Label label = createLabel(toolkit, composite, Messages.jmxSection);
		GridData data = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
		label.setLayoutData(data);
		jmxPortText = SWTUtil.createText(composite, SWT.SINGLE | SWT.BORDER, 1);
		jmxPortText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				handleEdit();
			}
		});

		initialize();
	}

	protected Label createLabel(FormToolkit toolkit, Composite parent,
			String text) {
		Label label = toolkit.createLabel(parent, text);
		label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		return label;
	}

	/**
	 * @see ServerEditorSection#dispose()
	 */
	public void dispose() {
		//do nothing
	}

	/**
	 * @see ServerEditorSection#init(IEditorSite, IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);

		// Cache workspace and default deploy paths
		if (server != null) {
			frameworkInstance = (IOSGIFrameworkInstance) server.loadAdapter(
					IOSGIFrameworkInstance.class, null);
			addChangeListeners();
		}
		initialize();
	}

	/**
	 * Initialize the fields in this editor.
	 */
	protected void initialize() {
		if (frameworkInstance == null)
			return;
		updating = true;

		int port = frameworkInstance.getJMXPort();
		boolean enabled = frameworkInstance.getJMXEnabled();

		if (jmxPortText != null) {
			jmxPortText.setText("" + port);
			jmxPortText.setEnabled(enabled);
		}
		if (jmxEnabled != null) {
			jmxEnabled.setSelection(enabled);
		}
		updating = false;
		validate();
	}

	protected void validate() {
		if (frameworkInstance != null && jmxEnabled != null && jmxPortText != null ) {
			boolean en = jmxEnabled.getSelection();
			String port = jmxPortText.getText();
			if (en) {
				try {
					Integer.parseInt(port);
				} catch (Exception ex) {
					setErrorMessage(Messages.jmxInvalidPort);
				}
			}
		}
		// All is okay, clear any previous error
		setErrorMessage(null);
	}

	void makeDirty() {
		//This command does nothing but execute sets the dirty flag
		//for the editor because the content of the target definition has
		//changed
		execute(new ServerChangedCommand(frameworkInstance));
	}

	protected void handleEdit() {

		if (frameworkInstance != null && updating == false) {
			boolean enabledNewValue = jmxEnabled.getSelection();
			String portNewValue = jmxPortText.getText();

			boolean enabledOldValue = frameworkInstance.getJMXEnabled();
			int portOldValue = frameworkInstance.getJMXPort();
			try {
				int p = portOldValue;
				if (enabledNewValue) {
					p = Integer.parseInt(portNewValue);
					if(portOldValue != p)
						frameworkInstance.setJMXPort(p);
				}
				frameworkInstance.setJMXEnabled(enabledNewValue);
				jmxPortText.setEnabled(enabledNewValue);
				if(p != portOldValue || enabledNewValue != enabledOldValue)
					makeDirty();
			} catch (Exception ex) {
				//If the port cannot be set (i.e. not int)
			}

		}

	}
}