package persistence;
/*
This class references code from JsonSerializationDemo
Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */

import org.json.JSONObject;

public interface Writable {
    //EFFECTS: returns this as a JSON object
    JSONObject thisToJson();
}
