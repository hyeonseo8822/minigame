package project;

import java.util.*;

public class ArrayListMain {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		ArrayList<String> n = new ArrayList<>();
		int max = 0;
		for(int i = 0; i < 4; i++) {
			System.out.print("이름 입력 : ");
			String name = s.next();
			n.add(name);
			if(n.get(max).length() < n.get(i).length()) {
				max = i;
			}
		}
		System.out.println(n.get(max));
	}
}