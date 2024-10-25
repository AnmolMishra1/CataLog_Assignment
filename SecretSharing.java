import java.math.BigInteger;

public class SecretSharingSolver {
    public static void main(String[] args) {
        // JSON input as a string
        String jsonString = """
        {
            "keys": {
                "n": 10,
                "k": 7
            },
            "1": {
                "base": "6",
                "value": "13444211440455345511"
            },
            "2": {
                "base": "15",
                "value": "aed7015a346d63"
            },
            "3": {
                "base": "15",
                "value": "6aeeb69631c227c"
            },
            "4": {
                "base": "16",
                "value": "e1b5e05623d881f"
            },
            "5": {
                "base": "8",
                "value": "316034514573652620673"
            },
            "6": {
                "base": "3",
                "value": "2122212201122002221120200210011020220200"
            },
            "7": {
                "base": "3",
                "value": "20120221122211000100210021102001201112121"
            },
            "8": {
                "base": "6",
                "value": "20220554335330240002224253"
            },
            "9": {
                "base": "12",
                "value": "45153788322a1255483"
            },
            "10": {
                "base": "7",
                "value": "1101613130313526312514143"
            }
        }""";

        // Manually parsing JSON
        int n = 10;
        int k = 7;

        // Decoded points (x, y)
        BigInteger[][] points = new BigInteger[k][2]; // To hold k points

        // Manually decode points
        points[0] = new BigInteger[]{BigInteger.valueOf(1), decodeFromBase("13444211440455345511", 6)};
        points[1] = new BigInteger[]{BigInteger.valueOf(2), decodeFromBase("aed7015a346d63", 15)};
        points[2] = new BigInteger[]{BigInteger.valueOf(3), decodeFromBase("6aeeb69631c227c", 15)};
        points[3] = new BigInteger[]{BigInteger.valueOf(4), decodeFromBase("e1b5e05623d881f", 16)};
        points[4] = new BigInteger[]{BigInteger.valueOf(5), decodeFromBase("316034514573652620673", 8)};
        points[5] = new BigInteger[]{BigInteger.valueOf(6), decodeFromBase("2122212201122002221120200210011020220200", 3)};
        points[6] = new BigInteger[]{BigInteger.valueOf(7), decodeFromBase("20120221122211000100210021102001201112121", 3)};

        // Calculate the secret (constant term) c using Lagrange interpolation at x = 0
        BigInteger secret = calculateSecret(points, k);
        
        // Output the secret
        System.out.println("Secret (constant term c): " + secret);
    }

    // Decode a value from a given base
    private static BigInteger decodeFromBase(String value, int base) {
        return new BigInteger(value, base);
    }

    // Calculate the secret using Lagrange interpolation
    private static BigInteger calculateSecret(BigInteger[][] points, int k) {
        BigInteger secret = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger x_i = points[i][0];
            BigInteger y_i = points[i][1];
            BigInteger term = y_i;

            for (int j = 0; j < k; j++) {
                if (j != i) {
                    BigInteger x_j = points[j][0];
                    term = term.multiply(x_i.negate()).divide(x_i.subtract(x_j));
                }
            }
            secret = secret.add(term);
        }

        return secret;
    }
}
