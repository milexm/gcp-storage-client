/**
 * 
 */
package com.acloudysky.storage;
import com.acloudysky.auth.AuthenticateGoogleServiceClient;

import com.acloudysky.auth.IGoogleServiceClientAuthentication;
import com.acloudysky.utilities.Utility;
import com.google.api.services.storage.Storage;

/**
 * Main class for the google-storage-client console application. 
 * @author Michael
 *
 */
public class Main {

	 
	/***
     * Application entry point which displays the start greetings and performs the 
     * following main tasks:
     * <ul>
     *      <li>Gets the authenticated client object authorized to access the 
     *      Google Storage service.</li> 
     *		<li>Reads the default settings.</li>
     * 		<li>Instantiates the operation classes.</li>
	 * 		<li>Delegates to the SimpleUI class the display of the selection 
	 *         menu and the processing of the user's input.</li>
	 * </ul>
	 * @see ServiceDefaultSettings#readSettings(String, String, String)  
	 * @see ProjectOperations#initProjectOperations(Storage, String)
     * @see BucketOperations#initBucketOperations(Storage, String, String)
     * @see ObjectOperations#initObjectOperations(Storage, ServiceDefaultSettings)
     * @see SimpleUI#SimpleUI()
	 * @param args; args[0] = "storage"
	 * 
	 */
	public static void main(String[] args) {
	
		// The API client name
		String client = null;
		
		// Set DEBUG flag for testing. 
		Utility.setDEBUG(false);
	
		// Display greeting message.
		Utility.displayWelcomeMessage("Google Storage Service");
		
		// Read input parameters.
		try {
			
				client = args[0];
		}
		catch (Exception e) {
			System.out.println("IO error trying to read application input! Assigning default values.");
			// Assign default values if none are passed.
			if (args.length==0) {
				client = "storage";
			}
			else {
				System.out.println("IO error trying to read application input!");
				System.exit(1); 
			}
		}
		
		if (Utility.isDEBUG()) {
			String startGreetings = String.format("Start %s console application", client);
			System.out.println(startGreetings);	
		}
		
		if (Utility.isDEBUG()) {
			// Display scopes.
			Utility.displayScopes(IGoogleServiceClientAuthentication.storageScopes);
			// Get scope.
			String scope =  String.format("Selected scope: %s", Utility.getScope(Utility.storageScopes, "dev_storage_full_control"));
			System.out.println(scope);	
		}
		
		if (Utility.isDEBUG())
			Utility.getAbsoluteFilePath(".googleservices", "storage", "client_secrets.json");
		
		// Instantiate the AuthenticateGoogleServiceClient class.
		AuthenticateGoogleServiceClient serviceAuthentication = 
				new AuthenticateGoogleServiceClient(".googleservices", "storage", "client_secrets.json"); 
		
		// Instantiate the StorageDefaultSettings class.
		StorageDefaultSettings defaultSettings = new StorageDefaultSettings();
		
		
		// Create an authenticated client which is authorized to use Google Storage API.
		Storage storageServiceClient = null;
		
		try {
				storageServiceClient = 
						serviceAuthentication.getAuthenticatedStorageClient(Utility.getScope(Utility.storageScopes, "dev_storage_FC"));
				String service = storageServiceClient.getApplicationName();
				if (Utility.isDEBUG()) {
					System.out.println(String.format("App name is: %s", service));
				}
		}
		catch (Exception e) {
			System.out.println(String.format("Error %s during service authentication.", e.toString()));
		}
			
		
		if (storageServiceClient != null) {
			
			
			// Read application default values from the JSON file and store them in memory.
			StorageDefaultSettings clientDefaultSettings = 
					defaultSettings.readSettings(".googleservices", "storage", "client_defaults.json");
			
			
			// Initialize Storage operations classes.
			ProjectOperations.initProjectOperations(storageServiceClient, clientDefaultSettings.getProject());
			BucketOperations.initBucketOperations(storageServiceClient, clientDefaultSettings.getProject(), 
					clientDefaultSettings.getBucket());
			ObjectOperations.initObjectOperations(storageServiceClient, clientDefaultSettings);
			
			
			// Instantiate SimpleUI class and display menu.
			SimpleUI sui = new SimpleUI();
	
			// Start loop to process user's input.
			sui.processUserInput();
		}
		else 
			String.format("Error %s", "service object is null.");

		// Display goodbye menu.
		Utility.displayGoodbyeMessage("Google Storage Service");	
	}

	
}

