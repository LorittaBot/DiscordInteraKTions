package net.perfectdreams.discordinteraktions.verifier

import org.bouncycastle.asn1.edec.EdECObjectIdentifiers
import org.bouncycastle.asn1.x509.AlgorithmIdentifier
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyFactory
import java.security.Security
import java.security.Signature
import java.security.spec.X509EncodedKeySpec

class InteractionRequestVerifier(
    publicKey: String,
) {
    companion object {
        /**
         * The algorithm used in Discord's interactions requests
         */
        private val INTERACTIONS_ALGORITHM = "ed25519"
        private val BOUNCY_CASTLE_PROVIDER = BouncyCastleProvider().also {
            // Add Bouncy Castle to Java's Security Provider
            Security.addProvider(it)
        }
    }

    private val publicKeyInfo = SubjectPublicKeyInfo(
        AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519),
        hex(publicKey)
    )

    private val kf = KeyFactory.getInstance(
        INTERACTIONS_ALGORITHM,
        BOUNCY_CASTLE_PROVIDER
    )
    private val pkSpec = X509EncodedKeySpec(publicKeyInfo.encoded)
    private val generatedPublicKey = kf.generatePublic(pkSpec)

    /**
     * Validates the request with [requestBody], [signature] and [timestamp] and returns if the request is valid or not
     *
     * @param requestBody The requests' POST body
     * @param signature   From the `X-Signature-Ed25519` header
     * @param timestamp   From the `X-Signature-Timestamp` header
     * @return if the request is valid
     */
    fun verifyKey(requestBody: String, signature: String, timestamp: String): Boolean {
        val signedData = Signature.getInstance(
            INTERACTIONS_ALGORITHM,
            BOUNCY_CASTLE_PROVIDER
        )
        signedData.initVerify(generatedPublicKey)

        signedData.update((timestamp + requestBody).toByteArray())
        return signedData.verify(hex(signature))
    }


    /**
     * Decode bytes from HEX string. It should be no spaces and `0x` prefixes.
     */
    private fun hex(s: String): ByteArray {
        // From Ktor "Crypto.kt" file
        val result = ByteArray(s.length / 2)
        for (idx in result.indices) {
            val srcIdx = idx * 2
            val high = s[srcIdx].toString().toInt(16) shl 4
            val low = s[srcIdx + 1].toString().toInt(16)
            result[idx] = (high or low).toByte()
        }

        return result
    }
}