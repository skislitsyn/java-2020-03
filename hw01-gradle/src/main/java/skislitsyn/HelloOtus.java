package skislitsyn;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

public class HelloOtus {

	public static void main(String[] args) {
		String s = "HelloOtus";
		String sHashed = Hashing.sha256()
				.hashString(s, StandardCharsets.UTF_8)
				.toString();
		System.out.println(sHashed);
	}

}
