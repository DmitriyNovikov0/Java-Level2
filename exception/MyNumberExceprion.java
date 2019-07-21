package exception;

public class MyNumberExceprion extends Exception {
    public MyNumberExceprion() {
        super();
    }

    @Override
    public String toString(){
        return "Ошибка преобразования строки в число! ";
    }
}
