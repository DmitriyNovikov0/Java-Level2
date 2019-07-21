package exception;

public class Main {
    static int M_SIZE = 4;

 //efsaegsegsr

    public static void main(String[] args) {
        try( MyMatrix matrix = new MyMatrix("1 3 1 2\n2 3 2 2\n5 6 7 1\n3 3 1 0", M_SIZE) ){
            System.out.println(matrix.outRes());
        } catch (MyException | MyNumberExceprion e){
            System.out.print(e.toString());
        }
//        matrix.printMatrix();
    }
}
