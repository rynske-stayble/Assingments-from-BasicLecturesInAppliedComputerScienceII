public class Hanoi{
	static void hanoi(int n, char a, char b, char c) {
    		if(n==0) {
        		return;
		}
    		else {
        		hanoi(n-1, a, c, b);
        		System.out.println("move "+n+": "+a+" --> "+c);
        		hanoi(n-1, b, a, c);
		}
	}

	public static void main (String arg[]) {
		hanoi(4, 'A', 'B', 'C');
	}
}

