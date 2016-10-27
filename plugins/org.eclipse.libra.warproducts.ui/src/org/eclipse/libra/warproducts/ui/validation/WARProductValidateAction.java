/******************************************************************************* 
* Copyright (c) 2010, 2011 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Holger Staudacher - initial API and implementation
*******************************************************************************/
package org.eclipse.libra.warproducts.ui.validation;

import java.util.*;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.libra.warproducts.core.IWARProduct;
import org.eclipse.libra.warproducts.core.validation.*;
import org.eclipse.libra.warproducts.ui.Messages;
import org.eclipse.osgi.service.resolver.ResolverError;
import org.eclipse.osgi.service.resolver.VersionConstraint;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.internal.core.TargetPlatformHelper;
import org.eclipse.pde.internal.core.iproduct.IProductPlugin;
import org.eclipse.pde.internal.launching.launcher.LaunchValidationOperation;
import org.eclipse.pde.internal.launching.launcher.ProductValidationOperation;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEPluginImages;

public class WARProductValidateAction extends Action {

  IWARProduct product;
  List<IValidationListener> listeners;

  public WARProductValidateAction( final IWARProduct product ) {
    super( Messages.ValidateActionTitle, IAction.AS_PUSH_BUTTON );
    setImageDescriptor( PDEPluginImages.DESC_VALIDATE_TOOL );
    this.product = product;
  }
  
  public void addValidationListener( final IValidationListener listener ) {
    if( listeners == null ) {
      listeners = new ArrayList<IValidationListener>();
    }
    if( !listeners.contains( listener ) ) {
      listeners.add( listener );
    }
  }

  public void run() {
    HashMap<String,IPluginModelBase> map = new HashMap<String,IPluginModelBase>();
    IProductPlugin[] plugins = product.getPlugins();
    for( int i = 0; i < plugins.length; i++ ) {
      String id = plugins[ i ].getId();
      addBundleIfExisting( map, id );
    }
    validate( map );
  }

  private static void addBundleIfExisting( final Map<String,IPluginModelBase> map, final String id ) {
    if( id != null && !map.containsKey( id ) ) {
      IPluginModelBase model = PluginRegistry.findModel( id );
      if( bundleExist( id, model ) ) {
        map.put( id, model );
      }
    }
  }
  
  private static boolean bundleExist( final String id, final IPluginModelBase model ) {
    boolean result = false;
    if( model != null ) {
      boolean matchesCurrentEnvironment 
      = TargetPlatformHelper.matchesCurrentEnvironment( model );
      if( matchesCurrentEnvironment ) {
        result = true;
      }
    }
    return result;
  }

  private void validate( final Map<String,IPluginModelBase> map ) {
    try {
      IPluginModelBase[] baseModel = new IPluginModelBase[ map.size() ];
      IPluginModelBase[] models  = map.values().toArray( baseModel );
      LaunchValidationOperation operation 
        = new ProductValidationOperation( models );
      operation.run( new NullProgressMonitor() );
      verifyResult( operation );
    } catch( final CoreException e ) {
      PDEPlugin.logException( e );
    }
  }

  private void verifyResult( final LaunchValidationOperation operation )
  {
    Map<Object,Object[]> errors = operation.getInput();
    verifyPDEErrors( errors );
    validateWarContent( errors );
    notifyListeners( errors );
  }

  private void notifyListeners( final Map errors ) {
    if( listeners != null ) {
      for( int i = 0; i < listeners.size(); i++ ) {
        IValidationListener listener = listeners.get( i );
        listener.validationFinished( errors );
      }
    }
  }

  private static void verifyPDEErrors( final Map<Object,Object[]> map ) {
    for(final Entry<?,?> entry: map.entrySet()){
      Object currentKey = entry.getKey();
      ResolverError[] errors = (ResolverError[]) entry.getValue();
      ResolverError[] validErrors = validateErrors( errors );
      map.remove( currentKey );
      if( validErrors.length > 0 ) {
        map.put( currentKey, validErrors );
      }
    }
  }
  
  private static ResolverError[] validateErrors( final ResolverError[] errors ) {
    List<ResolverError> validErrors = new ArrayList<ResolverError>();
    for( int i = 0; i < errors.length; i++ ) {
      ResolverError error = errors[ i ];
      VersionConstraint constraint = error.getUnsatisfiedConstraint();
      if( constraint != null ) {
        String unresolvedBundleId = constraint.getName();
        if (unresolvedBundleId==null){
        	continue;
        }
        
        if(    !isBanned( unresolvedBundleId ) 
            && !unresolvedBundleId.equals( Validator.SERVLET_BRIDGE_ID ) ) 
        {
          validErrors.add( error );
        }
      }
    }
    ResolverError[] result = new ResolverError[ validErrors.size() ];
    validErrors.toArray( result );
    return result;
  }

  private static boolean isBanned( final String unresolvedBundleId ) {
    boolean result = false;
    String[] bannedBundles = Validator.BANNED_BUNDLES;
    for( int i = 0; i < bannedBundles.length && !result; i++ ) {
      if( unresolvedBundleId.indexOf( bannedBundles[ i ] ) != -1 ) {
        result = true;
      }
    }
    return result;
  }

  private void validateWarContent( final Map<Object,Object[]> map ) {
    Validator validator = new Validator( product );
    Validation validation = validator.validate();
    if( !validation.isValid() ) {
      handleWARValidationErrors( map, validation.getErrors() );
    }
  }

  private static void handleWARValidationErrors( final Map<Object,Object[]> map, 
                                          final ValidationError[] errors ) 
  {
    String key = Messages.ValidateAction;
    map.put( key, errors );
  }
  
}
