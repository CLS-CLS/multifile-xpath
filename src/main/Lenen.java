package main;

public class Lenen {
	
	int[][] matrix;
	String source;
	String target;
	
	public Lenen(String source, String target) {
		this.source = source;
		this.target = target;
	}
	
	public void getDistance(){
		
		int sourceLength = source.length();
		int targetLength = target.length();
		
		initialiseMatrix(sourceLength, targetLength);
		
		
		
		
	}

	private void initialiseMatrix(int sourceLength, int targetLength) {
		matrix = new int[targetLength + 1][sourceLength +1];
		System.out.println("matrix[0].length = " + matrix[0].length + "\nmatrix.length= " +matrix.length);
		for (int i = 0; i < matrix[0].length; i++){
			matrix[0][i] = i;
		}
		for (int i=0; i<matrix.length; i++){
			matrix[i][0] = i;
		}
		
	}
	
	
	public void printMatrix(){
		for (int row = -1; row <matrix.length ; row++ ){
			System.out.println("\n");
			for (int col =-1; col <matrix[0].length ; col++){
				if (row ==-1) {
					if (col ==-1){
						System.out.print(" ");
						continue;
					}else if (col >0){
						System.out.print(source.charAt(col -1));
					}
				}else {
					if (col==-1){
						if (row >0){
							System.out.print(target.charAt(row -1));
							continue;
						}
					}else {
						System.out.print(matrix[row]
						                        [col]);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new Lenen("Chris", "Lytsikas").getDistance();
	}

}
