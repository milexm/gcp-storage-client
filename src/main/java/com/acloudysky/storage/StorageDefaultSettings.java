/** 
 * LEGAL: Use and Disclaimer. 
 * This software belongs to the owner of the http://www.acloudysky.com site and supports the
 * examples described there. 
 * Unless required by applicable law or agreed to in writing, this software is distributed on 
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. 
 * Please, use the software accordingly and provide the proper acknowledgement to the author.
 * @author milexm@gmail.com  
 **/
package com.acloudysky.storage;



import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.acloudysky.utilities.Utility;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;


/*** 
 * Reads the service client default settings from the related JSON file.
 * The file contains information such as project ID, default bucket name and so on.
 * The following is an example of the JSON formatted information:
 * <pre>
 *{
 * "defaultproject": "your project ID",
 * "defaultbucket":  "your default bucket",
 * "defaultobject":  "your default object",
 * "defaultprefix":  "yourself",
 * "defaultemail":   "you@gmail.com",
 * "defaultdomain":  "your domain name"
 *}
 * </pre>
 * @author Michael
 *
 */

public class StorageDefaultSettings extends GenericJson {
	

	@Key("defaultproject")
	private String project;
	
	@Key("defaultbucket")
	private String bucket;

	@Key("defaultobject")
	private String object;
	
	@Key("defaultprefix")
	private String prefix;

	@Key("defaultemail")
	private String email;

	@Key("defaultdomain")
	private String domain;

	public String getProject() {
		return project;
	}

	public String getBucket() {
		return bucket;
	}

	public String getObject() {
		return object;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public String getEmail() {
		return email;
	}

	public String getDomain() {
		return domain;
	}
  
	/***
	 * Default constructor to allow creation of an instance of the ServiceDefaultSettings 
	 * class through the jasonFactory.
	 */
	public StorageDefaultSettings() {
		
	}
	
	
	/***
	 * Read sample settings contained in the supporting <i>client_defaults.json</i> file.
	 * 	<b>Note</b>. This method uses {@link com.google.api.client.json.JsonFactory} to create
	 * a ServiceDefaultSettings object to read the JSON formatted information.
	 * @param parentDir The parent directory such as .googleservices.
	 * @param dataDir The directory containing the data file such as storage.
	 * @param dataFile The fiel containing the data such as client_defaults.json. 
	 * @return The ServiceDefaultSettings object
	 */
	public StorageDefaultSettings readSettings(String parentDir, String dataDir, String dataFile) {
		
		// Instance of the JSON factory. 
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

		StorageDefaultSettings settings = null;
		
		// Load application default settings from the "client_settings.json" file
		String filePath = Utility.getAbsoluteFilePath(parentDir, dataDir, dataFile);
		
		try {
				InputStream inputStream = new FileInputStream(filePath);
				// Create settings object to access the default settings.
				settings = jsonFactory.fromInputStream(inputStream, StorageDefaultSettings.class);
	      } catch (IOException e) {
	        String msg = String.format("Error occurred; %s", e.getMessage());
	        System.out.println(msg);
	      }
		if (settings.getProject().startsWith("Enter ") || settings.getBucket().startsWith("Enter ")) {
			System.out.println("Enter sample settings info "
					+ dataFile);
			System.exit(1);
		}
		return settings;
	} 
}

