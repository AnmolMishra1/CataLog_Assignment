import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class SecretSharing {

    public static void main(String[] args) {
        try {
            // Load and parse JSON data
            JSONObject testCase1 = new JSONObject(new JSONTokener(new FileReader("testcase1.json")));
            JSONObject testCase2 = new JSONObject(new JSONTokener(new FileReader("testcase2.json")));

            
            BigInteger secret1 = findSecretConstant(testCase1);
            BigInteger secret2 = findSecretConstant(testCase2);

            
            System.out.println("Secret for Test Case 1: " + secret1);
            System.out.println("Secret for Test Case 2: " + secret2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  
    public static BigInteger findSecretConstant(JSONObject testCase) {
        JSONObject keys = testCase.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        Map<Integer, BigInteger> points = new HashMap<>();

    
        for (String key : testCase.keySet()) {
            if (!key.equals("keys")) {
                int x = Integer.parseInt(key);
                JSONObject pointData = testCase.getJSONObject(key);
                int base = pointData.getInt("base");
                String value = pointData.getString("value");
                BigInteger y = new BigInteger(value, base);
                points.put(x, y);
            }
        }

        // Apply Lagrange interpolation
        return lagrangeInterpolation(points, k);
    }

    // Lagrange interpolation to find the constant term (c)
    public static BigInteger lagrangeInterpolation(Map<Integer, BigInteger> points, int k) {
        BigInteger result = BigInteger.ZERO;

        for (Map.Entry<Integer, BigInteger> i : points.entrySet()) {
            int xi = i.getKey();
            BigInteger yi = i.getValue();

            BigInteger term = yi;
            for (Map.Entry<Integer, BigInteger> j : points.entrySet()) {
                int xj = j.getKey();
                if (xi != xj) {
                    term = term.multiply(BigInteger.valueOf(-xj)).divide(BigInteger.valueOf(xi - xj));
                }
            }
            result = result.add(term);
        }

        return result;
    }
}
