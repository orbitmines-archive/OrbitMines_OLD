package com.vexsoftware.votifier.net.protocol.v1crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;

public class RSAKeygen {

    public RSAKeygen() {}

    public static KeyPair generate(int bits) throws Exception {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(bits, RSAKeyGenParameterSpec.F4);

        keygen.initialize(spec);
        return keygen.generateKeyPair();
    }
}
