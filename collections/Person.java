package collections;

import java.util.ArrayList;
//не знаю почему по заданию person  вообще логичнее records назвать класс
public class Person {
    private ArrayList<String> phones = new ArrayList<>();

    public Person(String phone){
        this.phones.add(phone);
    }

    public void addPhone(String phone){
        this.phones.add(phone);
    }

    public ArrayList<String> getPhone(){
        return phones;
    }

    @Override
    public String toString(){
        String sTmp = "Телефоны: ";

        for(int i = 0; i <  phones.size(); i++){
            if(i > 0){
                sTmp += ",";
            }
            sTmp +=  " " + phones.get(i);
        }
        return sTmp;
    }

}
