import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.ObjectOutputStream
import java.net.ServerSocket
import kotlin.random.Random

const val heartRateInterval = 1000L
const val port = 9002

fun main() {
    fakeHeartRateServerSocket()
}

fun fakeHeartRateServerSocket() {
    val ss = ServerSocket(port)
    ss.use {
        while (true) {
            println("Waiting for clients")
            val socket = ss.accept()
            println("Found you, ${socket.inetAddress.hostAddress}!")
            socket.use {
                val writer = ObjectOutputStream(socket.getOutputStream())
                runBlocking {
                    while (true) {
                        val next = Random.nextInt(75, 79)
                        delay(heartRateInterval)
                        try {
                            withContext(Dispatchers.IO) {
                                writer.writeInt(next)
                                writer.flush()
                                println("wrote $next")
                            }
                        } catch (e: IOException) {
                            println("Write failed")
                            break
                        }
                    }
                }
            }
        }
    }
}