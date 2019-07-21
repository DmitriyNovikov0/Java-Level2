package exception;

import java.io.Closeable;

public class MyMatrix implements Closeable {
    final String DELIMETER1 = "\n";
    final String DELIMETER2 = " ";

//по условию преобразовать строку в двумерный массив типа String[][];
    private String[][] matrix;


    public MyMatrix(String inStr, int sizeArray) throws MyException{
        int a;
        matrix = new String[sizeArray][sizeArray];
        String[] strArr1 = inStr.split(DELIMETER1);
        if(strArr1.length != sizeArray) {
            throw new MyException(String.valueOf(sizeArray) );
        }
        for(a = 0; a < strArr1.length; a++){
//наверное это не очень эффективно, но работает
            String[] strArr2 = strArr1[a] .split(DELIMETER2);
            if(strArr2.length != sizeArray) {
                throw new MyException(String.valueOf(sizeArray));
            }
            System.arraycopy(strArr2, 0, matrix[a], 0, strArr2.length);
        }
    }

    public float outRes() throws MyNumberExceprion {
        int iTmp = 0;
        for(int i = 0;  i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                try {
                    iTmp += Integer.parseInt(matrix[i][j]);
                } catch (NumberFormatException e){
                    throw new MyNumberExceprion();
                }
            }
        }

        return iTmp / 2;
    }

    public void printMatrix() {
        for(int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[i].length; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void close(){}

}
