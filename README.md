# Android: SpinnerView with Firebase Example
A simple android project in which there is a client end and an admin end. 
The Client can access the real-time database which is actually a .csv converted into a .json file and imported to the firebase database. (ie; converted row and column data to key and value pair). The client has to choose two keys from the spinner list and the resulting value is printed on the screen.
Instead row and column access, we search the value like key1=>key2=>"value".(More or Less similar to row and column) 
  
The admin can broadcast messages (solution that works but not advisable) and can add/delete the data in the database.

There are four activities , including MainActivity.java

MainActivity.java contains two buttons to access either User Page or the Admin page.

In UserEnd.java,
We fetch the data from firebase database. The root key of our JSON structure is called "Blends" , which contains certain blends , ie; row values as keys from our CSV file (which is converted into JSON later on), within that we have our column values as keys. The equivalent of our ValueOf[i][j] from our CSV file in our json file can be written as "key1" => "key2" => corrosponding value.




DatabaseReference ref = FirebaseDatabase.getInstance().getReference("BLENDS").child(blend1_Choice);
ref.addListenerForSingleValueEvent(new ValueEventListener() {
	@Override
	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
		String mixStr = (String) dataSnapshot.child(blend2_Choice).getValue();
		mixText.setText(mixStr);        /// for the final output value text.
	}
                        
                        

NOTE: The spinnerview values can be populated only under valuelistener since the values are dynamic and subject to change, values will not be displayed if its done the static way. [Solution through experience]


reference.addListenerForSingleValueEvent(new ValueEventListener() {
...
 final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,keyValues);
  autoComplete1.setAdapter(adapter);
  autoComplete2.setAdapter(adapter);
  autoComplete1.setThreshold(1);
  autoComplete2.setThreshold(1);
  autoComplete1.setFocusable(true);
  autoComplete2.setFocusable(true);
....
  });


checkForAdminBroadCast() is just for receiving admin messages by detecting any changes in the "ADMIN_MESSAGE" key in our json file.

AdminEnd.java contains admin authentication using firebase auth.

AdminConsole.java contains modifying data functions , sending broadcast message by adding or modifying values under the "ADMIN_MESSAGE" key.




