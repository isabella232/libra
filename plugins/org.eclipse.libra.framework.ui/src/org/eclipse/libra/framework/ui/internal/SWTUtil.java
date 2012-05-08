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

package org.eclipse.libra.framework.ui.internal;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
/**
 * SWT Utility class.
 */
public class SWTUtil {
	private static FontMetrics fontMetrics;

	protected static void initializeDialogUnits(Control testControl) {
		// Compute and store a font metric
		GC gc = new GC(testControl);
		gc.setFont(JFaceResources.getDialogFont());
		fontMetrics = gc.getFontMetrics();
		gc.dispose();
	}

	/**
	 * Returns a width hint for a button control.
	 */
	protected static int getButtonWidthHint(Button button) {
		int widthHint = Dialog.convertHorizontalDLUsToPixels(fontMetrics, IDialogConstants.BUTTON_WIDTH);
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
	}

    /**
     * Sets width and height hint for the button control.
     * <b>Note:</b> This is a NOP if the button's layout data is not
     * an instance of <code>GridData</code>.
     * 
     * @param    the button for which to set the dimension hint
     */
    public static void setButtonDimensionHint(Button button) {
        Object gd = button.getLayoutData();
        if (gd instanceof GridData) {
            ((GridData) gd).widthHint = getButtonWidthHint(button);
            ((GridData) gd).horizontalAlignment = GridData.FILL;
        }
    }
	   
	/**
	 * Create a new button with the standard size.
	 * 
	 * @param comp the component to add the button to
	 * @param label the button label
	 * @return a button
	 */
	public static Button createButton(Composite comp, String label) {
		Button b = new Button(comp, SWT.PUSH);
		b.setText(label);
		if (fontMetrics == null)
			initializeDialogUnits(comp);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		data.widthHint = getButtonWidthHint(b);
		b.setLayoutData(data);
		return b;
	}
	
    /**
     * Creates and returns a new radio button with the given
     * label.
     * 
     * @param parent parent control
     * @param label button label or <code>null</code>
     * @param hspan number of columns to horizontally span in the parent composite
     * @return a new radio button
     */
    public static Button createCheckButton(Composite parent, String label, int hspan) {
        Button button = new Button(parent, SWT.CHECK);
        button.setFont(parent.getFont());
        if (label != null) {
            button.setText(label);
        }
        GridData gd = new GridData();
        gd.horizontalSpan = hspan;
        button.setLayoutData(gd);
        return button;
    }	
	
    /**
     * Creates a new text widget 
     * 
     * @param parent the parent composite to add this text widget to
     * @param style the style bits for the text widget
     * @param hspan the horizontal span to take up on the parent composite
     * @param fill the fill for the grid layout
     * @return the new text widget
     */

   public static Text createText(Composite parent, int style, int hspan) {
      Text t = new Text(parent, style);
      t.setFont(parent.getFont());
      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalSpan = hspan;
      t.setLayoutData(gd);
      return t;
   }

}