package pl.edu.pg.eti.data.util

import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

fun OkHttpClient.Builder.trustAllSslCertificates(): OkHttpClient.Builder {
    val trustAllCerts: Array<TrustManager> = arrayOf(
        object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<X509Certificate?>?,
                authType: String?
            ) {
                // skip checking if client is trusted
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<X509Certificate?>?,
                authType: String?
            ) {
                // skip checking if server is trusted
            }

            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return arrayOf()
            }
        }
    )

    val sslContext: SSLContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, SecureRandom())

    val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

    this.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
    this.hostnameVerifier { _, _ -> true }

    return this
}