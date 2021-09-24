public HashMap<Integer, String> getNames(){

 HashMap<Integer, String> data = new HashMap<Integer, String>();

 try{
  SQLiteOpenHelper helper = new MyOpenDbHelper(context);
  SQLiteDatabase db = helper.getReadableDatabase();
  String selectQuery = "SELECT  * FROM names";
  Cursor cursor = db.rawQuery(selectQuery, null);
  if (cursor != null && cursor.moveToFirst()){ //make sure you got results, and move to first row
    do{
        int mID = cursor.getInt(0); //column 0 for the current row
        String mName = cursor.getString(1); //column 1 for the current row
        data.put(mID, mName);

      } while (cursor.moveToNext()); //move to next row in the query result

  }

 } catch (Exception ex) {
    Log.e("MyApp", ex.getMessage());
 } finally
 {
    if (cursor != null) {
       cursor.close();

    }
    if (db != null) {
        db.close();
    }

 }

return data;
}