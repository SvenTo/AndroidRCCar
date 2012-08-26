/*******************************************************************************
 * Copyright (C) 2012 Sven Nobis
 * 
 * This file is part of AndroidRCCar (http://androidrccar.sven.to)
 * 
 * AndroidRCCar is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 ******************************************************************************/
package to.sven.androidrccar.client.presentation;

import to.sven.androidrccar.client.R;
import to.sven.androidrccar.client.service.impl.ClientConfigurationService;
import to.sven.androidrccar.common.model.ConnectionParameter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * This Activity let the user input the connection parameters for the connection to the host. 
 * @author sven
 *
 */
public class ConnectionParamAcitivity extends Activity {
	
	/**
	 * Hostname input
	 */
	private TextView hostnameEdit;
	
	/**
	 * Password input
	 */
	private TextView passwordEdit;
	
	/**
	 * Remember Settings Check Box
	 */
	private CheckBox rememberSettingsCB;
	
	/**
	 * A {@link ClientConfigurationService} for storing the parameters, if requested.
	 */
	private ClientConfigurationService configurationService;
	
	/**
	 * The {@link TextView} above the input fields.
	 */
	private TextView welcomeTextView;	
	
	/**
	 * Id of the options menu
	 * @see #onCreateOptionsMenu
	 */
	protected int menu_id = R.menu.menu;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection_param);
		
		configurationService = new ClientConfigurationService(this);
		
		loadSettingsFromConfig();
	}
	
	/**
	 * Loads the settings from the configuration. If password equals null,
	 * there are no settings.
	 */
	private void loadSettingsFromConfig() {
		ConnectionParameter savedParams = configurationService.getConnectionParameter();
		welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
		hostnameEdit = (TextView) findViewById(R.id.hostnameEdit);
		passwordEdit = (TextView) findViewById(R.id.passwordEdit);
		rememberSettingsCB = (CheckBox) findViewById(R.id.rememberSettingsCB);
		
		if(savedParams.password != null) {
			rememberSettingsCB.setChecked(true);
			hostnameEdit.setText(savedParams.toString());
			passwordEdit.setText(savedParams.password);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(menu_id, menu);
	    return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menu_about: showAbout(); 
	                            break;
	        case R.id.menu_settings: showSettings(); 
	                            break;
	    }
	    return true;
	}
	
	/**
	 * Shows the settings.
	 */
	private void showSettings() {
        Intent settingsActivity = new Intent(getBaseContext(), ClientPreferenceActivity.class);
        startActivity(settingsActivity);
	}

	/**
	 * Shows the about dialog.
	 */
	private void showAbout() {
		 final Dialog aboutDialog = new Dialog(this, android.R.style.Theme_Dialog);
		 aboutDialog.setContentView(R.layout.info);
		 aboutDialog.setTitle(R.string.action_about);
         TextView textView = (TextView) aboutDialog.findViewById(R.id.infoTextVersion); 
         textView.setText(R.string.app_version);
         textView = (TextView) aboutDialog.findViewById(R.id.infoAuthor); 
 		 Linkify.addLinks(textView, Linkify.ALL);
 		 
         Button okBtn = (Button)aboutDialog.findViewById(R.id.info_ok_btn);
         okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				aboutDialog.dismiss();
			}
		 });
         
         aboutDialog.show();
	}

	/**
	 * Starts the {@link ClientActivity} if valid data was input.
	 * @param v Not used
	 */
	public void connectClick(@SuppressWarnings("unused") View v) {
		ConnectionParameter params;
		try {
			params = new ConnectionParameter(hostnameEdit.getText().toString(),
	 									     passwordEdit.getText().toString());
		} catch(Exception ex) {
			welcomeTextView.setText(R.string.inputs_invalid);
			return;
		}
		
		if(rememberSettingsCB.isChecked()) {
			configurationService.saveConnectionParameter(params);
		} else {
			configurationService.resetSavedConnectionParameter();
		}
		
		Intent intent = new Intent(this, ClientActivity.class);
		intent.setData(Uri.parse(params.toConnectionURL()));
		startActivity(intent);
	}
}
