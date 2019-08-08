package exception;

public class MyException extends Exception {
    private String sTmp;

    public MyException(String sTmp) {
        super();
        this.sTmp = sTmp;
    }

    @Override
    public String toString(){
        return "Ошибка!!! Размер полученной из строки матрицы отличен от " + sTmp + "x" + sTmp;
    }
}
