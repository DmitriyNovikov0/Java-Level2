package collections;


import java.util.HashMap;

public class PhoneBook {
    private HashMap<String, Person> phonebook = new HashMap<>();

    public void add(String firstName, String phone){
        if (phonebook.containsKey(firstName)) {
            phonebook.get(firstName).addPhone(phone);
        } else {
            Person person = new Person(phone);
            phonebook.put(firstName, person);
        }
    }

    public void get(String firstName){
        if(phonebook.containsKey(firstName)){

            System.out.println(phonebook.get(firstName));
        }
    }


}
